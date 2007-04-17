package ecologylab.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import ecologylab.appframework.StatusReporter;
import ecologylab.generic.Debug;
import ecologylab.generic.DispatchTarget;
import ecologylab.generic.Downloadable;
import ecologylab.net.ParsedURL;

public class DownloadableFileToDisk 
extends Debug
implements Downloadable, DispatchTarget
{
	private boolean		downloadDone 		= false;
	private boolean 	downloadStarted 	= false;
	
	private InputStream inputStream 		= null;
	
	private ParsedURL	target;
	private File		destination;
	
	private static final int BUFFER_SIZE	= 8192;
	private StatusReporter status 			= null;
	private int			fileSize			= -1;
	
	public DownloadableFileToDisk(ParsedURL target, File destination, StatusReporter status)
	{
		this.target 		= target;
		this.destination 	= destination;
		this.status			= status;
	}
	
	public DownloadableFileToDisk(ParsedURL target, File destination)
	{
		this(target, destination, null);
	}
	
	public void downloadDone()
	{
		downloadDone = true;
	}

	public void handleIoError()
	{
		// TODO Auto-generated method stub

	}

	public boolean handleTimeout()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDownloadDone()
	{
		return downloadDone;
	}

	public void performDownload() throws Exception
	{
		debug("performDownload() top");
		if (downloadStarted)
			return;
		
		downloadStarted = true;
		
		//this gets the stream and sets the member field 'fileSize'
//		inputStream = getInputStream(zipSource);
		inputStream = target.url().openStream();
		
		debug("performDownload() got InputStream");

		//actually read and write the file
		// if the file already exists, delete it
		if (destination.exists())
		{
			boolean deleted = destination.delete();
			debug("File exists, so deleting = " + deleted);
		}
		
		  OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination));
		  debug("performDownload() got outputStream from " + destination);

		  byte fileBytes[] = new byte[BUFFER_SIZE];
		  
		  //Read data from the source url and write it out to the file
          int count 		= 0;
          int lastTenth 	= 1;
          int incrementSize = fileSize/10;
          //int i=0;
          while(( count = inputStream.read(fileBytes, 0, BUFFER_SIZE)) != -1 )
          {
        	  if (status != null)
        	  {
        		  //Our status will be in 10% increments
        		  if (count >=  incrementSize*(lastTenth))
        			  status.display("Downloading file " + destination.getName(),
        				  					1, count/10, incrementSize);
        		  
        		  //can't just increment because we maybe skip/hit 1/10ths due to 
        		  //faster/slower transfer rates, traffic, etc.
        		  lastTenth = (int) Math.floor(((double)count/fileSize)*10);
        	  }
        	  outputStream.write(fileBytes, 0, count);
          }
		  
          outputStream.close();
          inputStream.close();
          
          synchronized (this)
          {
        	  downloadDone	= true;
          }
	}

	public void delivery(Object o)
	{
		System.out.println("Finished download file: " + target + " -> " + destination);
	}
	
}
