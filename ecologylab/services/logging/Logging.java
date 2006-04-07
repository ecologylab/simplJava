package ecologylab.services.logging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Vector;

import ecologylab.generic.Files;
import ecologylab.generic.Generic;
import ecologylab.generic.Memory;
import ecologylab.generic.PropertiesAndDirectories;
import ecologylab.generic.StringTools;
import ecologylab.generic.ConsoleUtils;
import ecologylab.services.ServicesClient;
import ecologylab.services.SessionId;
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
	public static final long	time					= System.currentTimeMillis();

	protected BufferedWriter	writer;
	ServicesClient 				loggingClient = null;
	NameSpace 					nameSpace;
	
	Thread						thread;

	int							log_mode;
	
	static String						date					= new Date(time).toString();
	
	/**
	 * Logging Header message string written to the logging file in the begining  
	 */
	public static final String LOG_HEADER	= XmlTools.xmlHeader() + 
						"\n<session_log " + "ip=\"" + localHost() + "\" starting_time=\"" + date() + "\">" +
 						"\n<op_sequence>\n\n";
	/**
	 * Logging closing message string written to the logging file at the end
	 */
	public static final String LOG_CLOSING	= "\n</op_sequence></session_log>\n\n";

	static final int NOLOG =0;
	static final int LOGTODESKTOP = 1;
	static final int LOGTOSERVICESSERVER = 2; 
	
	File						logFile		= null;
	String						logFileName = null;
	

	/**
	 * Queue of action opperations that have been sent to us for loggin.
	 * Our Runnable Thread will actually to the file writes,
	 * at a convenient time, at a low priority.
	 */
	LogOps 						opSet	= new LogOps();
	
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
	   setLogMode();
	}
	
    void setLogMode()
    {
    	log_mode = Generic.parameterInt("log_mode", 0); 	
	  switch (log_mode)
	  {
	  	case NOLOG: 
	  		break;
	  	case LOGTODESKTOP:
	  		// The application save a log file only if there is the application directory exists 
	  		// Or, if the application directory can be created
	  		if( PropertiesAndDirectories.thisApplicationDir() != null )
	  		{
				logFile 	= new File(PropertiesAndDirectories.logDir(), logFileName);
				writer		= Files.openWriter(logFile);
				debugA("logging to " + logFile + " " + writer);
	  		}
	  		else
	  			debug("thisApplicationDir() does not exist or cannot create");
  	        break;   
	  	case LOGTOSERVICESSERVER:  
  		  	//emit which interface the subject used in the study
 // 	        studyInterface= Generic.parameter("userinterface");
	  		/**
	  		 * Create the logging client which communicates with the logging server
	  		 */
  	        loggingClient = new ServicesClient(LoggingDef.loggingServer, LoggingDef.port, nameSpace);
  	        break;
	  	        
	    default: break;
	  }
    }
    
	public void logAction(MixedInitiativeOp op)
	{

	   if ( (writer != null) || (loggingClient!=null) )
		   opsToWrite.add(op);

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
	public void stop()
	{
		if (thread != null)
		{
			finished	= true;
			writeQueuedActions();
			thread		= null;
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
		String actionStr = "";

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
//		ConsoleUtils.obtrusiveConsoleOutput("opSet is built. translating to xml.");
		if( writer != null )
		{
			try 
			{
				actionStr = (String)opSet.translateToXML(false);
			} 
			catch (XmlTranslationException e) 
			{
				e.printStackTrace();
			}
			Files.writeLine(writer, actionStr);
		}
		
		if( loggingClient != null )
		{
			debug("Logging: Sending opSet " + opSet);
			loggingClient.sendMessage(opSet);
		}
		
		opSet.clearSet();
	}
	
	public void setDate(String value)
    {
   		date	=	value;
    }

    
/**
 * Write the start of the log header out to the log file
 * OR, send the begining logging file message so that logging server write the start of the log header.
 */
	public void beginEmit()
	{
		if( loggingClient != null )
		{
			debug("Logging: Sending BeginEmit ");
			loggingClient.sendMessage(new BeginEmit());
		}
		
		if (writer !=null)
		{
			Files.writeLine(writer, LOG_HEADER);
		}

	}
	
	/**
	 * Write the closing() XML to the log file, and close it.
	 * Or, send the closing logging file message to the logging server
	 */
	public void endEmit()
	{
		if( loggingClient != null )
			loggingClient.sendMessage(new EndEmit());
		
		if (writer != null)
		{
		   debug("Logging: Sending EndEmit "+ LOG_CLOSING);
		   stop();
		   Files.writeLine(writer, LOG_CLOSING);
		   debug("wrote line");
		   Files.closeWriter(writer);
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
   
   String logFileName()
   {
   	 return "user" + SessionId.get() + "-logFile" + date() + ".xml";
   }     
   
   String traceFileName()
   {
   	 return "user" + SessionId.get() + "-TraceFile" + date() + ".trace";
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

   /**
    * Return our session start timestamp.
    * @return
    */
 	public static final long time()
 	{
 		return time;
 	}
}
