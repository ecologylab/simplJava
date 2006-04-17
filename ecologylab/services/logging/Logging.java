package ecologylab.services.logging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Vector;

import ecologylab.generic.Files;
import ecologylab.generic.Generic;
import ecologylab.generic.Memory;
import ecologylab.generic.PropertiesAndDirectories;
import ecologylab.generic.StringTools;
import ecologylab.services.ServicesClient;
import ecologylab.services.SessionId;
import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpace;
import ecologylab.xml.XmlTools;
import ecologylab.xml.XmlTranslationException;

/**
 * Logging service. 
 * Uses ecologylab.xml to serialize user and agent actions to a file.
 * Can also provide the stuff as a String, if you specify it.
 *
 * @author andruid
 */
public class Logging
extends ElementState
implements Runnable
{
	private static final String SESSION_LOG_START = "\n<session_log>\n ";
	static final String OP_SEQUENCE_START	= "\n\n<op_sequence>\n\n";
	static final String OP_SEQUENCE_END		= "\n</op_sequence>\n";
	
	/**
	 * Logging closing message string written to the logging file at the end
	 */
	public static final String LOG_CLOSING	= "\n</op_sequence></session_log>\n\n";
	
	/**
	 * Logging Header message string written to the logging file in the begining  
	 */
	static final String BEGIN_EMIT	= XmlTools.xmlHeader() + SESSION_LOG_START;
	
	/**
	 * This field is used for reading a log in from a file, but not for writing one, because
	 * we dont the write the log file all at once, and so can't automatically translate
	 * the start tag and end tag for this element.
	 */
	public ArrayListState		opSequence;
	
	protected BufferedWriter	writer;
	ServicesClient 				loggingClient = null;
	NameSpace 					nameSpace;
	
	Thread						thread;

	int							logMode;
	
	static final int NO_LOGGING				= 0;
	static final int LOG_TO_FILE			= 1;
	static final int LOG_TO_SERVICES_SERVER = 2;
	
	static final int MAX_OPS_BEFORE_WRITE	= 20;
	
	File						logFile		= null;
	String						logFileName = null;
	
/**
 * Object for sending a batch of ops to the LoggingServer.
 */
	LogOps 						opSet	= new LogOps();
	
	/**
	 * Queue of action opperations that have been sent to us for logging.
	 * Our Runnable Thread will actually to the file writes,
	 * at a convenient time, at a low priority.
	 */
	Vector						opsToWrite	= new Vector();
	
	boolean						finished;

	static final int			THREAD_PRIORITY	= 1;
	static final int			SLEEP_TIME		= 15000;
	
	public Logging(NameSpace nameSpace, String logFileName)
	{
		super();
		finished = false;
		this.nameSpace = nameSpace;
		this.logFileName = logFileName;
		int log_mode = Generic.parameterInt("log_mode", NO_LOGGING);
		this.logMode	= log_mode;
		switch (log_mode)
		{
		case NO_LOGGING: 
			break;
		case LOG_TO_FILE:
			if (logFileName == null)
			{
				log_mode	= NO_LOGGING;
			}
			else
			{
				File logDir	= PropertiesAndDirectories.logDir();
				if (logDir == null)
				{
					log_mode= NO_LOGGING;
					debug("Can't write to logDir=" + logDir);
				}
				else
				{
					logFile 	= new File(logDir, logFileName);
					writer		= Files.openWriter(logFile);
					if (writer != null)
						debugA("logging to " + logFile + " " + writer);
					else
						debug("ERROR: cant open writer to " + logFile);
				}
			}
			break;
		case LOG_TO_SERVICES_SERVER:  
			/**
			 * Create the logging client which communicates with the logging server
			 */
			loggingClient = new ServicesClient(LoggingDef.loggingServer, LoggingDef.port, nameSpace);
			if (loggingClient.connect())
				debug("Logging to service via connection: " + loggingClient);
			else
			{
				loggingClient	= null;
				log_mode		= NO_LOGGING;
			}
			break;
			
		default: break;
		}
	}
	
	public synchronized void logAction(MixedInitiativeOp op)
	{

	   if ( (writer != null) || (loggingClient != null) )
	   {
		   opsToWrite.add(op);
		   if ((loggingClient != null) && (opsToWrite.size() > MAX_OPS_BEFORE_WRITE))
		   {
			   debugA("interrupting thread to do i/o now");
			   thread.interrupt(); // end sleep in that thread prematurely to do i/o
		   }
	   }
	}
	
	public void start()
	{
		if (thread == null)
		{
			thread = new Thread(this);
			thread.setPriority(THREAD_PRIORITY);
			thread.start();
		}
	}
	
	public synchronized void stop()
	{
		if (thread != null)
		{
			finished	= true;
			thread		= null;
			writeQueuedActions();
			writeEpilogue();
			if (writer != null)
			{
				try
				{
					writer.close();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
				writer	= null;
			}
			if (loggingClient != null)
			{
				loggingClient.disconnect();
				loggingClient	= null;
			}
		}
	}
	
	long lastGcTime;
	static final long KICK_GC_INTERVAL		= 300000; // 5 minutes
	
	/**
	 * Logging to a file is delayed to the actions of this thread, because
	 * otherwise, it can mess up priorities in the system, because events get
	 * logged in the highest priority thread.
	 */
	public void run()
	{
		lastGcTime	= System.currentTimeMillis();	
		while (!finished)
		{
			Thread.interrupted();
			Generic.sleep(SLEEP_TIME);		
			writeQueuedActions();
			long now	=  System.currentTimeMillis();
			long deltaT	= now - lastGcTime;
			if (deltaT >= KICK_GC_INTERVAL)
			{
				lastGcTime	= now;
				Memory.reclaim();
			}
		}
	}

	/**
	 * 1) Send the logging data to the Logging server 
	 * 2) Write the logging data to the local file 
	 * 
	 * Either 1) or 2) will be performed based on the selected option
	 */
	protected void writeQueuedActions() 
	{

//		ConsoleUtils.obtrusiveConsoleOutput("opSet is built. translating to xml.");
		if( writer != null )
		{
			try 
			{
				String actionStr = "";
				synchronized (opsToWrite)
				{
					int num	= opsToWrite.size();
					for (int i=0; i< num; i++)
					{
						MixedInitiativeOp thatOp	= (MixedInitiativeOp) opsToWrite.get(i);
						actionStr += (String)thatOp.translateToXML(false) + "\n";	
					}
					opsToWrite.clear();
				}
				Files.writeLine(writer, actionStr);
			} 
			catch (XmlTranslationException e) 
			{
				e.printStackTrace();
			}
		}
		if( loggingClient != null )
		{
			synchronized (opsToWrite)
			{
				int num	= opsToWrite.size();
				for (int i=0; i< num; i++)
				{
					MixedInitiativeOp thatOp	= (MixedInitiativeOp) opsToWrite.get(i);
					// copy thatOp to opSet
					opSet.addNestedElement(thatOp);		
				}
				opsToWrite.clear();
			}
			debug("Logging: Sending opSet " + opSet);
			loggingClient.sendMessage(opSet);
			opSet.clearSet();
		}
	}
	
	/**
	 * A message at the beginnging of the log.
	 * This method may be overridden to return a subclass of Prologue, by subclasses of this,
	 * that wish to emit application specific information at the start of a log.
	 * 
	 * @return
	 */
	protected Prologue getPrologue()
	{
		return new Prologue(this);
	}
	
	/**
	 * A message at the end of the log.
	 * This method may be overridden to return a subclass of Epilogue, by subclasses of this,
	 * that wish to emit application specific information at the end of a log.
	 * 
	 * @return
	 */	
	protected Epilogue getEpilogue()
	{
		Epilogue epilogue = new Epilogue(this);
		return epilogue;
	}
	
	/**
	 * Write the start of the log header out to the log file
	 * OR, send the begining logging file message so that logging server write the start of the log header.
	 */
	public void writePrologue()
	{
		if (logMode != NO_LOGGING)
		{
			Prologue prologue = getPrologue();
			if( loggingClient != null )
			{
				int uid = Generic.parameterInt("uid", 0);
				debug("Logging: Sending Prologue userID:" + uid);
				prologue.setUserID(uid);
				loggingClient.sendMessage(prologue);
			}
			if (writer !=null)
			{
				Files.writeLine(writer, prologue.getMessageString());
			}
		}
	}

	/**
	 * Write the closing() XML to the log file, and close it.
	 * Or, send the closing logging file message to the logging server
	 */
	public void writeEpilogue()
	{
		if (logMode != NO_LOGGING)
		{
			Epilogue epilogue = getEpilogue();
			if( loggingClient != null )
			{
				debug("Logging: Sending Epilogue "+ LOG_CLOSING);
				loggingClient.sendMessage(epilogue);
			}
			
			if (writer != null)
			{
//				stop();
				Files.writeLine(writer, epilogue.getMessageString());
				debug("wrote line");
				Files.closeWriter(writer);
			}
		}
	}
	
	
	//TODO this looks like dead code.
	public ElementState loadLogXML(String fileName, NameSpace nameSpace)
	{			
		
		fixBrokenLog(fileName);
		
		// build the state object from the XML
		ElementState stateObject = null;
		try
		{
			stateObject = (ElementState) translateFromXML(fileName, nameSpace);
		} catch (XmlTranslationException e)
		{
			e.printStackTrace();
		}
		return stateObject;		
	}
	
	void fixBrokenLog(String fileName)
	{
		String lastLine = null;
		String lastSecondLine = null;
		String currentLine = null;
		BufferedReader reader		= Files.openReader(fileName);
		if (reader == null)
		{
			println("CANT OPEN LOGGING FILE: " + fileName);
			return;
		}
		currentLine = Files.readLine(reader);
		while(currentLine!=null)
		{
			lastLine = currentLine;			
			currentLine = Files.readLine(reader);
			if(currentLine!=null)
			{
				lastSecondLine = lastLine;
				lastLine = currentLine;				
			}			
		}		
		Files.closeReader(reader);
		
		//println("LatSL is  " + lastSecondLine);
		//println("LatL is  " + lastLine);
				
		if ((lastLine == null)||(!lastLine.equals("</collage_log>")))
		{
		   BufferedWriter writer = Files.openWriter(fileName, true);
		   if((lastSecondLine == null) || (!lastSecondLine.equals("</collage_op_sequence>")))
		   {
//		   	  debug(3,"write last second line");
		   	  Files.writeLine(writer, "/n</collage_op_sequence>");
		   }	
		   
//		   debug(0,"write last line");
		   Files.writeLine(writer, "/n</collage_log>");
		   
		   Files.closeWriter(writer);		   
		}				
	}	
   
	/**
	 * Re-formatted starting time string of the current session.
	 * @return String   
	 */
   static String date()
   {
	   final String date	=  new Date(System.currentTimeMillis()).toString();;
	   String temp;
	   temp=date.replace(' ','_');
	   temp=temp.replace(':','-');   	
	   return temp;
   }
   

   static String localHost = null;
   /**
    * local host address (parse out only IP address)
    * @return
    */
   static String localHost()
   {
	   if( localHost == null )
	   {
		   try {
			localHost = InetAddress.getLocalHost().toString();
	//		localHost = localHost.replace('/','_');
			localHost = localHost.substring(localHost.indexOf('/')+1);
			return localHost;
		   } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			   e.printStackTrace();
			}
	   }
	   return localHost;
   }

   void copyTraceFile(File outputFile)
   {
      final String TRACE_PATH = System.getProperty("deployment.user.logdir") +
	 "/plugin"+ StringTools.remove(System.getProperty("java.version"),'_')+
	 ".trace";
   		BufferedWriter      writer;
		BufferedReader      reader;
	    reader = Files.openReader(TRACE_PATH);
	    writer = Files.openWriter(outputFile);

	    String oneLine = Files.readLine(reader);
	    
	    while (oneLine!=null)
	    {
			Files.writeLine(writer, oneLine);
			oneLine = Files.readLine(reader);    	
	    }
	    Files.closeReader(reader);
	    Files.closeWriter(writer);
   }

   static final long time	= System.currentTimeMillis();
   /**
    * Return our session start timestamp.
    * @return
    */
 	public static final long time()
 	{
 		return time;
 	}
}
