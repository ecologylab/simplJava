package ecologylab.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import ecologylab.generic.Files;
import ecologylab.generic.Generic;
import ecologylab.generic.Memory;
import ecologylab.generic.PropertiesAndDirectories;
import ecologylab.generic.StringTools;
import ecologylab.services.messages.BeginEmit;
import ecologylab.services.messages.EndEmit;
import ecologylab.services.messages.LogOps;
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
	public String				studyInterface          = null;

	protected BufferedWriter	writer;
	ServicesClient 				loggingClient = null;
	NameSpace 					nameSpace;

	int							log_mode;
	
	static String						date					= new Date(time).toString();

	static final String LOG_FILE_NAME		= "combinFormationLog.xml";
	
	public static final String LOG_HEADER	= XmlTools.xmlHeader() + "\n<logging_data>" +
						"\n<localhost_ip>" + localHost() + "</localhost_ip>\n" +
						"\n<starting_time>" + date() + "</starting_time>\n" +
 						"\n<op_sequence>\n\n";
	public static final String LOG_CLOSING	= "\n</op_sequence>\n\n";

	static final int NOLOG =0;
	static final int LOGTODESKTOP = 1;
	static final int LOGTOSERVICESSERVER = 2; 
	
	File						logFile		= null;
	
	String 						readLogFileName ="";
	

	/**
	 * Queue of action strings that have been sent to us for loggin.
	 * Our Runnable Thread will actually to the file writes,
	 * at a convenient time, at a low priority.
	 */
	LogOps 						opSet	= new LogOps();
	boolean						finished;

	static final int			THREAD_PRIORITY	= 1;
	static final int			SLEEP_TIME		= 15000;
	
	public Logging(NameSpace nameSpace)
	{
	   super();
	   finished = false;
	   this.nameSpace = nameSpace;
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
  			//logFile = new File(LOG_FILE_NAME);
			logFile 	= new File(PropertiesAndDirectories.desktopDir(), LOG_FILE_NAME);
			writer		= Files.openWriter(logFile);
			debugA("logging to " + logFile);
  	        break;
	  	case LOGTOSERVICESSERVER:  
  		  	//emit which interface the subject used in the study
  	        studyInterface= Generic.parameter("userinterface");
  	        loggingClient = new ServicesClient(LoggingDef.loggingServer, LoggingDef.port, nameSpace);

	  	        
	  	        break;
	  	        
	    default: break;
	  }
    }
    
	public void logAction(MixedInitiativeOp op)
	{

	   if ( (writer != null) || (loggingClient!=null) )
	   	  opSet.addNestedElement(op);

	}
		
	public void start()
	{
		Thread thread = new Thread(this);
		thread.setPriority(THREAD_PRIORITY);
		thread.start();
	}
	public void stop()
	{
		finished	= true;
		writeQueuedActions();
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


	protected void writeQueuedActions() 
	{
		String actionStr = "";
		
		if( loggingClient != null )
		{
			debug("Logging: Sending opSet " + opSet);
			loggingClient.sendMessage(opSet);
		}
		
		if( writer != null )
		{
			try 
			{
				actionStr = (String)opSet.translateToXML(false);
			} catch (XmlTranslationException e) 
			{
				e.printStackTrace();
			}
			Files.writeLine(writer, actionStr);
		}
		
		opSet.clearSet();

	}
	public void setDate(String value)
    {
   		date	=	value;
    }

    public void setStudyInterface(String value)
    {
        studyInterface	 = value;
    }
    
    public String studyInterface ()
    {
    	return studyInterface;
    }
    
/**
 * Write the start of the log out to the log file, if there is one.
 * Also, to the wholeLog String, if the doingUserStudy pref is true.
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
			start();
		}

	}
	
	/**
	 * Write the closing() XML to the log file, and close it.
	 * <p/>
	 * Generates current TermSet and TraversableSet.
	 * 
	 * @param infoCollector  The agent that has TraversableSet state that needs to be emitted.	
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
   
   static String date()
   {
	   String temp;
	   temp=date.replace(' ','_');
	   temp=temp.replace(':','-');   	
	   return temp;
   }
   
   static String localHost = null;
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
   
   void writeStudyInterfaceFile(File outputFile)
   {
   	    if (!outputFile.exists())
   	    {
	   		BufferedWriter      writer;
	   		writer = Files.openWriter(outputFile);
	   		Files.writeLine(writer, studyInterface);
	   		Files.closeWriter(writer);
   	    }
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
