package ecologylab.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ecologylab.appframework.StatusReporter;
import ecologylab.concurrent.BasicSite;
import ecologylab.concurrent.Downloadable;
import ecologylab.concurrent.DownloadableLogRecord;
import ecologylab.generic.Continuation;
import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;

public class DownloadableFileToDisk 
extends Debug
implements Downloadable, Continuation<Object>
{
	private boolean		downloadDone 		= false;
	private boolean 	downloadStarted 	= false;

	private InputStream inputStream 		= null;

	private OutputStream outputStream;

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

	public DownloadableFileToDisk(ParsedURL target, InputStream inputStream, File destination, StatusReporter status)
	{
		this(target, destination, status);
		this.inputStream	= inputStream;
	}

	public DownloadableFileToDisk(ParsedURL target, File destination)
	{
		this(target, destination, null);
	}

	public DownloadableFileToDisk(ParsedURL target, InputStream inputStream, File destination)
	{
		this(target, inputStream, destination, null);
	}

	@Override
	public void handleIoError(Throwable e)
	{
		closeStreams();
		downloadDone = true;
	}

	public boolean isDownloadDone()
	{
		return downloadDone;
	}

	@Override
	public void performDownload() throws IOException
	{
		debug("performDownload() top");
		if (downloadStarted)
			return;

		downloadStarted = true;

		//this gets the stream and sets the member field 'fileSize'
		//		inputStream = getInputStream(zipSource);
		if (inputStream == null)
			inputStream = target.url().openStream();

		debug("performDownload() got InputStream");

		//actually read and write the file
		// if the file already exists, delete it
		//FIXME -- consider using the existing, instead of deleting it!!!
		if (destination.exists())
		{
			boolean deleted = destination.delete();
			debug("File exists, so deleting = " + deleted);
		}

		outputStream 		= new BufferedOutputStream(new FileOutputStream(destination));
		debug("performDownload() got outputStream from " + destination);

		byte fileBytes[] 	= new byte[BUFFER_SIZE];

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

		closeStreams();

		synchronized (this)
		{
			downloadDone	= true;
		}
	}

	public void closeStreams()
	{
		try
		{
			if (outputStream != null)
			{
				OutputStream oStream		= this.outputStream;
				this.outputStream			= null;
				oStream.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			if (inputStream != null)
			{
				InputStream iStream			= this.inputStream;
				this.inputStream			= null;
				iStream.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void callback(Object o)
	{
		System.out.println("Finished download file: " + target + " -> " + destination);
	}

	@Override
	public boolean isRecycled()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BasicSite getSite()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedURL location()
	{
		return target;
	}
	/**
	 * 
	 * @return	What to tell the user about what is being downloaded.
	 */
	@Override
	public String message()
	{
		return null;
	}

	@Override
	public void recycle()
	{
	}

	/**
	 * Default empty implementation; will be ignored for this type.
	 */
	@Override
	public boolean isImage()
	{
		return false;
	}

	@Override
	public BasicSite getDownloadSite()
	{
		return null;
	}

	@Override
	public ParsedURL getDownloadLocation()
	{
		// TODO Auto-generated method stub
		return location();
	}

  @Override
  public boolean isCached()
  {
    // TODO Auto-generated method stub
    return false;
  }

	@Override
	public DownloadableLogRecord getLogRecord()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
