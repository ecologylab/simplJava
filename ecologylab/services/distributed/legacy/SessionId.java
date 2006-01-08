package ecologylab.services;

import java.io.*;

import ecologylab.generic.*;

/**
 * User study subject session number, and directory management.
 */
public class SessionId
extends Debug
{
   public static final int UNINITIALIZED	= -1;
   public static final int NO_SESSION_ID	= -2;
   
   static int	sessionId	= UNINITIALIZED;     
   
     //sessionID file path
    protected static String    sessionFileName     = "/studies/sessionid.txt";    
    protected static String    sessionFileDir      = "//ecology1/e$/";
    
    protected static String FILE_SEPARATOR         = System.getProperty("file.separator");
    protected static String SESSION_SEPARATOR      = "-";
    
    protected static String    sessionIdString           = "";
     
   
   public static int increment()
   {
      get();
      sessionId++;
      Integer newSessionId = new Integer(sessionId);
      BufferedWriter writer = Files.openWriter(sessionFileDir + FILE_SEPARATOR + sessionFileName);
      println("the newSessionId is " + newSessionId.toString());
      Files.write(writer, newSessionId.toString());
      Files.closeWriter(writer);
      File sessionLoggingDir	= getLogFileDir();
      sessionLoggingDir.mkdir();      
      //writeFile(sessionFileDir + FILE_SEPARATOR + sessionFileName, newSessionId.toString());
      return sessionId;
   }
   
   public static int get()
   {
      if (sessionId == UNINITIALIZED)
      {
	 BufferedReader reader	= null;
	 try
	 {
	    reader = Files.openReader(sessionFileDir + FILE_SEPARATOR + sessionFileName);
	 } catch (java.security.AccessControlException e)
	 {
	    println("Cant access session id file; catching exception:");
	    e.printStackTrace();
	 }
      	 if (reader == null)
      	 {
      	   println("the bufferedReader is null");
      	   return NO_SESSION_ID;
      	 }
      	 sessionIdString = Files.readLine(reader);      	       	 
      	 Files.closeReader(reader);
      	 println(" the sessionIdString is "+ sessionIdString);
	    // sessionIdString = readFile(sessionFileDir + FILE_SEPARATOR + sessionFileName); 
	     sessionIdString.trim();
	     sessionId = Integer.parseInt(sessionIdString);
      }
      return sessionId;
   }   
   
   public static String getStudyPath()
   {
   	 return "//ecology1/e$/studies/engaging1/";
   }
   public static String getLogFilePath()
   {   	 
   	 return (SessionId.get()==NO_SESSION_ID)?null:getStudyPath() + "user" + SessionId.get() + "/";
   }
   public static File getLogFileDir()
   {
   	 return new File(getLogFilePath());
   }
   public static File getLogFile(String fileName)
   {
      return new File(getLogFileDir(), fileName);
   }
   public static boolean logFileExists(String fileName)
   {
      File file	= new File(getLogFileDir(), fileName);
      return file.exists();
   }
}
