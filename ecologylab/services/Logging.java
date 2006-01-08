package ecologylab.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Date;
import java.util.Vector;


import ecologylab.generic.Files;
import ecologylab.generic.Generic;
import ecologylab.generic.Memory;
import ecologylab.generic.PropertiesAndDirectories;
import ecologylab.generic.StringTools;
import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;
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
	static final long	time					= System.currentTimeMillis();
	public String		date					= new Date(time).toString();
	public String		studyInterface          = null;

	protected BufferedWriter	writer;

	static final String LOG_FILE_NAME	= "combinFormationLog.xml";

	int							log_mode;
	
	static final int NOLOG =0;
	static final int LOGTODESKTOP = 1;
	static final int LOGTOUSERSTUDY = 2; 
	
	static File					logFile		= null;
    static File 				traceFile	= null;
    static File					interfaceFile = null;
	
	String 						readLogFileName ="";
	
	public Logging()
	{
	   super();
	   boolean doingUserStudy = Generic.parameterBool("doingUserStudy", false);
	   if (doingUserStudy)
		  wholeLog				= new StringBuffer(32 * 1024);
	}
	
	/**
	 * Used for creating CollageOps, a service provided to Undo
	 */

	/**
	 * Queue of action strings that have been sent to us for loggin.
	 * Our Runnable Thread will actually to the file writes,
	 * at a convenient time, at a low priority.
	 */
	Vector 						actionsToWrite	= new Vector(30);
	boolean						finished;
	
	StringBuffer				wholeLog;

	static final int			THREAD_PRIORITY	= 1;
	static final int			SLEEP_TIME		= 15000;
	
    void setLogMode()
    {
    	log_mode = Generic.parameterInt("log_mode", 0);
	  switch (log_mode)
	  {
	  	case NOLOG: break;
	  	case LOGTODESKTOP:
	  			//logFile = new File(LOG_FILE_NAME);
				logFile = new File(PropertiesAndDirectories.desktopDir(), LOG_FILE_NAME);
				debugA("logging to " + logFile);
	  	        break;
	  	case LOGTOUSERSTUDY:  
	  		  	//emit which interface the subject used in the study
	  	        studyInterface= Generic.parameter("userinterface");
		   final String logFilePath =
		      SessionId.getLogFilePath() + logFileName();

		   final String traceFilePath =
		      SessionId.getLogFilePath() + traceFileName();
		   final String interfaceFilePath =
		      SessionId.getLogFilePath() + "studyInterface.txt";
		   
	  	        if(SessionId.getLogFilePath()!=null)
	  	        {
	  				System.out.println("log file to study " + logFilePath);
	  	        	logFile = new File(logFilePath);	
	  	        	traceFile = new File(traceFilePath); 
	  	        	interfaceFile = new File(interfaceFilePath); 	        	        	  	        		         
	  	        }
	  	        else	  	        
	  	        logFile = new File(LOG_FILE_NAME);	    	  	        	  	        	  	        	  	        
	  	        break;
	  	        
	    default: break;
	  }
    }
    
	public void logAction(MixedInitiativeOp op)
	{
		try
		{
	//		collageOpSequence.add(op);
		   String actionStr	= op.translateToXML(false) + "\n";
		   //debugA("logAction("+actionStr);
		   if (writer != null)
		   {
//		      Files.writeLine(writer, actionStr);
		   	  actionsToWrite.add(actionStr);
		      // if ((count++ % 5) == 0) writer.flush();
		   }
		   if (wholeLog != null)
			  wholeLog.append(actionStr);
		}catch(XmlTranslationException e)
		{
			e.printStackTrace();
		}
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

	/**
	 * Stuff to emit at closing.
	 * 
	 * @return
	 */
	protected String epilog()
	{
		return "\n" + "</op_sequence>" + "\n\n";
	}
	/**
	 * Get the CollageLog as a String.
	 * Note: this only works if the startup pref doingUserStudy is set
	 * to true.
	 * <p/>
	 * Generates current TermSet and TraversableSet.
	 * 
	 * @param infoCollector  The agent that has TraversableSet state that needs to be emitted.	
	 */
	public String getWhole()
	{
	   wholeLog.append(epilog());
	   return wholeLog.toString();
	}
	/**
	 * 
	 */
	protected void writeQueuedActions() 
	{
		synchronized (actionsToWrite)
		{
			//debugA("writeQueuedActions[" + actionsToWrite.size());
			while (!actionsToWrite.isEmpty())
			{
				String actionStr	= (String) actionsToWrite.remove(0);
			    Files.writeLine(writer, actionStr);
			}
		}
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
		setLogMode();
		String ourStart			= "";
		String opSequenceStart	= "<op_sequence>\n\n";

		try 
		{
		   ourStart				= translateToXML(false, false) + "\n";
		} catch (XmlTranslationException e1) 
		{
		   e1.printStackTrace();
		}

		writer		= Files.openWriter(logFile);
		if (writer !=null)
		{
			Files.writeLine(writer, XmlTools.xmlHeader());
			Files.writeLine(writer, ourStart);
			Files.writeLine(writer, opSequenceStart);
			start();
		}
		if (wholeLog != null)
		{
		   wholeLog.append(XmlTools.xmlHeader());
		   wholeLog.append(ourStart);
		   wholeLog.append(opSequenceStart);
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
		String epilog	= epilog();
		
		if (writer != null)
		{
		   debugA("endEmit() "+ epilog);
		   stop();
		   Files.writeLine(writer, epilog);
		   debug("wrote line");
		   Files.closeWriter(writer);
		}
//		debug(1, "endEmit() end");
		if (log_mode == LOGTOUSERSTUDY)
		{
		  writeStudyInterfaceFile(interfaceFile);
          copyTraceFile(traceFile);	         	
		}
	}
	
/*	static public void setLogFile(String fileName)
	{
		logFilePath	= fileName;
		logFile		= new File(fileName);
	}
*/	
	//TODO this looks like dead code.
	public ElementState loadLogXML(String fileName)
	{			
		
		fixBrokenLog(fileName);
		
		// build the state object from the XML
		ElementState stateObject = null;
		try
		{
			stateObject = (ElementState) translateFromXML(fileName);
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
   
   String date()
   {
	   String temp;
	   temp=date.replace(' ','-');
	   temp=temp.replace(':','-');   	
	   return temp;
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
