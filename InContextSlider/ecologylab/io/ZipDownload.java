/**
 * Simple class to download and write zip files to disk.
 */
package ecologylab.generic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import ecologylab.gui.AWTBridge;
import ecologylab.gui.Status;

/**
 * Class implementing DownloadLoadable to allow for the downloading and writing
 * to disk of zip files. Additionally files can be extracted after downloaded. 
 * 
 * @author Blake Dworaczyk
 */
public class ZipDownload 
implements Downloadable, DispatchTarget
{
	
	static DownloadMonitor downloadMonitor = new DownloadMonitor("Zip DownloadMonitor", 0);
	
	ParsedURL 	zipSource;
	File		zipTarget;
	Status		status;
	boolean		keepStatus			= false;
	
	boolean		downloaded 			= false;
	boolean 	downloadStarted 	= false;
	boolean 	aborted				= false;
	boolean		extractWhenComplete = false;
	int			fileSize			= -1;
	
	InputStream inputStream = null;
	
	private static final int BUFFER_SIZE	= 8192;
	
	public ZipDownload(ParsedURL zipSource, File zipTarget, Status status)
	{
		super();
		
		this.zipSource 				= zipSource;
		this.zipTarget	 			= zipTarget;
		this.status					= status;
		
		if (status != null)
			keepStatus = true;
	}
	
	public ZipDownload(ParsedURL zipSource, File zipTarget)
	{
		this(zipSource, zipTarget, null);
	}
	
	/**
	 * Initiate the download and writing of the zip file. This
	 * is called by outsiders.
	 */
	public void downloadAndWrite(boolean extractWhenComplete)
	{
		this.extractWhenComplete 	= extractWhenComplete;
		downloadMonitor.download(this, this);
	}
	/**
	 * @see downloadAndWrite(boolean)
	 */
	public void downloadAndWrite()
	{
		downloadAndWrite(false);
	}

	/**
	 * ONLY called by DownloadMonitor to actually download the 
	 * zip file! Not called by outsiders!
	 */
	public void performDownload() 
	throws Exception 
	{
		if (downloadStarted)
			return;
		
		downloadStarted = true;
		
		//this gets the stream and sets the member field 'fileSize'
		inputStream = getInputStream(zipTarget);
		
		  //actually read and write the zip
		  OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(zipTarget));
		  byte zipBytes[] = new byte[BUFFER_SIZE];
		  
		  //Read data from the source file and write it out to the zip file
          int count 		= 0;
          int lastTenth 	= 1;
          int incrementSize = fileSize/10;
          //int i=0;
          while(( count = inputStream.read(zipBytes, 0, BUFFER_SIZE)) != -1 )
          {
        	  if (status != null)
        	  {
        		  //Our status will be in 10% increments
        		  if (count >=  incrementSize*(lastTenth))
        			  status.displayStatus("Downloading zip file " + zipTarget.getName(),
        				  					1, count/10, incrementSize);
        		  
        		  //can't just increment because we maybe skip/hit 1/10ths due to 
        		  //faster/slower transfer rates, traffic, etc.
        		  lastTenth = (int) Math.floor(((double)count/fileSize)*10);
        	  }
        	  outputStream.write(zipBytes, 0, count);
          }
		  
          outputStream.close();
          inputStream.close();
          
          if (extractWhenComplete)
        	  extractZipFile(zipTarget);
	}
	
	public BufferedInputStream getInputStream(File zipTarget)
	throws Exception
	{
		URLConnection	urlConnection;
		
		if (zipTarget.isDirectory())
		{
			  
			throw new Exception("ZipDownload: write exception! Can't write to directory!");
		}
		
		if (zipSource.isFile())
	    {
			inputStream	= new FileInputStream(zipSource.file());
			
	    }  
		//In case the purl was constructed with a local file used as a URL	
		//TODO find the source of these bad constructions! (only applicabale when cF is launched as an application)
	    else if (zipSource.toString().startsWith("file://"))
		  {
			  try
			  {
				File fileInput = new File(zipSource.toString().substring(7, zipSource.toString().length()));
				inputStream = new FileInputStream(fileInput);
			  }
			  catch (Exception e)
			  {
				  System.out.println("ZipDownload: error reading local file");
				  e.printStackTrace();
				  throw new Exception("ZipDownload: IOException");
			  }
		  }
		  else
		  {
			  urlConnection	= zipSource.url().openConnection();
		      if ((urlConnection == null) || aborted)
		      {
				 System.err.println("Cant open URLConnection for " + aborted+" "+zipSource);
				 throw new Exception("ZipDownload: IOException");
			  }
			  try
			  {
				urlConnection.connect();
				fileSize = urlConnection.getContentLength();
			  	inputStream	= urlConnection.getInputStream();
			  } 
			  catch (FileNotFoundException e)
			  {
			  	System.err.println("FileNotFound!");
			  	throw new Exception("ZipDownload: IOException");
			  }
		  }
		  if ((inputStream == null) || aborted)
		  {
		  	System.err.println("Cant open InputStream for " + aborted+" "+zipSource);
		  	throw new Exception("ZipDownload: IOException");
		  }
		  
		  return new BufferedInputStream(inputStream);
	}

	public boolean isDownloadDone() 
	{
		return downloaded;
	}

	public boolean handleTimeout() 
	{
		if (!downloaded && !aborted)
		{
			aborted = true;
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					
					return false;
				}
				return true;
			}
		}
		
		
		return true;
	}

	public void handleIoError() 
	{
		System.err.println("IO Error while download zip file: " + zipSource);
	}
	
	public void delivery(Object o)
	{
		System.out.println("ZipDownload delivered: + " + ((ZipDownload) o));
	}
	
	/**
	 * Extracts a zip file into the directory where it resides
	 * 
	 * @param zipSource	The source zip file to extract
	 */
	public static void extractZipFile(File zipSource)
	throws IOException
	{
		ZipFile zipFile 	= new ZipFile(zipSource);
		int numEntries 		= zipFile.size();
		int currentEntry	= 0;
		
		File targetDir 	= zipSource.getParentFile();
		FileOutputStream outputStream;
		int bytesRead;
		byte[] buffer;
		
		ZipInputStream zipInputStream = 
			new ZipInputStream(new FileInputStream(zipSource));
		
		ZipEntry entry;
		
		while ((entry = zipInputStream.getNextEntry()) != null)
		{
			if (entry.isDirectory())
			{
				File entryDir = new File(targetDir, entry.getName());
				if (!entryDir.exists())
					entryDir.mkdirs();
				
				continue;
			}
			//zipInputStream 		= zipFile.getInputStream(entry);
			File destFile		= new File(targetDir, entry.getName());
			outputStream		= 
				new FileOutputStream(destFile);
			 
			buffer = new byte[(int) entry.getSize()];
			while ((bytesRead = zipInputStream.read(buffer)) != -1) 
			{
                 
					outputStream.write(buffer, 0, bytesRead);
			}
			
			//give the user some feedback on how long this takes.
			currentEntry++;
			Generic.status("Zip file extracted: " + destFile);
		}
		
		System.out.println("Finished extracting Zip file: " + zipSource);
	}

}
