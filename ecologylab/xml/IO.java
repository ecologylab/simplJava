package ecologylab.xml;

import java.io.*;
import java.lang.reflect.Field;

/**
 * This class contains methods for doing all the I/O required. It has facilities
 * for printing debug messages and the File I/O required for xml reading/writing.   
 * @author      Andruid Kerne
 * @author      Madhur Khandelwal
 * @version     0.5
 */

public class IO 
{
	static final char	sep					= File.separatorChar;
	static final String	indent		= "\t";   
	static String		writeErrorString 	= "Error writing to file ";
	BufferedWriter		fileWriter;

/**
 * Print a debug message that starts with this.toString().
 */
   public void debug(String message)
   {
	  println(this, message);
   }
   public void debugA(String message)
   {
	  println(getClass()+"."+ message);
   }

   public static void debug(Object o, String message, Exception e)
   {
	  println(o, message);
	  e.printStackTrace();
   }
   public static void println(Object o, String message)
   {
	  println(o + "." + message);
   }

   public static void println(String message) 
   {   	
	  System.err.println(message);
   }
   
   public static BufferedWriter openWriter(String oFileName)
   {
	  return openWriter(newFile(oFileName));
   }
   
   public static File newFile(String inFileName)
   {
	  return newFile(null, inFileName);
   }
   
   public static BufferedWriter openWriter(File oFile)
   {
	  return openWriter(oFile, 1);
   }

   public static File newFile(File context, String inFileName)
   {
	  // one of the replaces should do something;
	  // and the other should do nothing.
	  String fixedFileName      =inFileName.replace('/',sep).replace('\\',sep);
	  File one	= new File(fixedFileName);
	  if (context == null)
		 return one;
	
	  if (one.isAbsolute() || (context == null))
	  {
		 // hack gnarly windows path shit
		 // (drives & backslashes are a plague on all programmers)
		 if (fixedFileName.charAt(1) != ':')
		 {
			String contextName	= context.getPath();
			if (contextName.charAt(1) == ':')
			{
			   String fixedFixed= contextName.substring(0,2) + fixedFileName;
			   return new File(fixedFixed);
			}
		 }
			 return one;
	  }

	 return new File(context, fixedFileName);
   }
  
   public boolean writeLine(String toWrite)
   {
	  return writeLine(fileWriter, toWrite);
   }
   
   public static boolean writeLine(BufferedWriter writer, String toWrite)
   {
	  if (toWrite == null)
	 	return true;
	  
	  boolean	ok		= true;
	  try
	  {  
		 writer.write(toWrite);
		 writer.newLine();
	  }
	  catch(IOException e) 
	  {
	  	ok = false;
	  }
	  
	  if (!ok)
		 System.err.println(writeErrorString + writer);
	  
	  return ok;
   }

   public static BufferedWriter openWriter(String oFileName, boolean append)
   {
	  return openWriter(newFile(oFileName), 1, append);
   }
   
   public static BufferedWriter openWriter(File oFile, int debugLevel)
   {
	  if(oFile==null)
		  return null;
   	  
	  BufferedWriter writer	= null;

	  String	oFileFullPath	= oFile.getAbsolutePath();

	  println("Files.openWriter(" + oFileFullPath);
      
	 try
	 {  
		writer	= new BufferedWriter (new FileWriter(oFile));
		println("\tOutput file: ");
		println(indent + indent + oFileFullPath);
	 }catch(IOException e)
	 {
		IO.println(writeErrorString + oFileFullPath);
	 }
	  return writer;
   }

   public static BufferedWriter openWriter(File oFile, int debugLevel, boolean append)
   {
	  if(oFile==null)
		  return null;
	  BufferedWriter writer	= null;

	  String	oFileFullPath	= oFile.getAbsolutePath();

	  println("Files.openWriter(" + oFileFullPath);
      
	  try
	  {  
		writer	= new BufferedWriter (new FileWriter(oFile, append));
		println("\tOutput file: ");
		println(indent + indent + oFileFullPath);
	  }	catch(IOException e)
	  {
		IO.println(writeErrorString + oFileFullPath);
	  }
	  return writer;
   }
   
   public static boolean closeWriter(BufferedWriter writer)
   {
	  boolean ok	= true;
	  try
	  {
		 writer.close();
	  } catch (IOException outE)
	  { 
	  	ok = false; 
	  }
	  return ok;
   }
	public static String errorString(Field f)
	{
	   return "Error setting field " + f.getName() + " ";
	}
}
