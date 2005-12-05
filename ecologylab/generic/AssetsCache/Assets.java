/**
 * The Assets class is used to manage cachable assets.
 */
package ecologylab.generic.AssetsCache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import ecologylab.generic.Debug;
import ecologylab.generic.DispatchTarget;
import ecologylab.generic.DownloadMonitor;
import ecologylab.generic.Generic;
import ecologylab.generic.ParsedURL;
import ecologylab.generic.ZipDownload;
import ecologylab.gui.Status;
import ecologylab.media.IIOPhoto;

/**
 * Used to manage cachable assets
 * 
 * @author blake
 */
public class Assets
extends Debug
{
	static File cacheRoot = null;
	
	/**
	 * current known assets (although arbitrary ones can exist)
	 */
	public static final String INTERFACE		= "interface";
	public static final String SEMANTICS		= "semantics";
	
	private static DownloadMonitor downloadMonitor;	
	
	//////////////////////////////////////////////////////////////
	
	/**
	 * No instances possible, static references only.
	 */
	private Assets() {}
	
	/**
	 * Sets the root file path for caching. Assets are specified relative to this path.
	 * @param cacheRoot The root file path for caching assets.
	 */
	public static void setCacheRoot(File cacheRoot)
	{
		Assets.cacheRoot = cacheRoot;
	}
	
	/**
	 * Given a relative path, return a file reference to this path
	 * from the cache root.
	 * 
	 * @param relativePath	A string representing the relative file path.
	 * @return	A file reference to the requested path
	 */
	public static File getAsset(String relativePath)
	{
		if (cacheRoot == null)
			return null;
		
		return new File(cacheRoot.getAbsolutePath() + File.separatorChar + relativePath);
	}
	
	/**
	 *  Same as getAsset(String), but allows additional relative file/directory 
	 *  to be specified against the relativePath
	 *  
	 * @param relativePath A string representing the relative file path
	 * @param additionalContext A string representing an additional relative path.
	 * This path is relative to the relativePath parameter (rather than the cache root).
	 * @return	A file reference to the requested path
	 */
	public static File getAsset(String relativePath, String additionalContext)
	{
		if (cacheRoot == null)
			return null;
		
		return new File(getAsset(relativePath), additionalContext);
	}
	
	/**
	 * Same as getAsset(String), but creates the Asset location if it
	 * doesn't exist
	 * 
	 * @param relativePath	A string representing the relative file path. 
	 * @return	A file reference tot he requested path
	 * @see getAsset(String)
	 */
	public static File getAndPerhapsCreateAsset(String relativePath)
	{
		File theAsset = getAsset(relativePath);
		
		if (!theAsset.exists())
			theAsset.mkdirs();
		
		return theAsset;
	}
	
	/**
	 * Same as getAndPerhapsCreateAsset(String, String), but creates the Asset location if it
	 * doesn't exist
	 * 
	 * @param relativePath	A string representing the relative file path. 
	 * @return	A file reference tot he requested path
	 * @see getAsset(String, additionalContext)
	 */
	public static File getAndPerhapsCreateAsset(String relativePath, String additionalContext)
	{
		File theAsset = getAsset(relativePath, additionalContext);
		
		if (!theAsset.exists())
			theAsset.mkdirs();
		
		return theAsset;
	}
	
	public static void downloadZip(ParsedURL sourceZip, File targetFile)
	{
		downloadZip(sourceZip, targetFile, null);
	}
	
	/**
	 * Convenience function to allow downloading and uncompressing of a 
	 * zip file from a source to a target location with minimal effort.
	 * 
	 * @param source The location of the zip file to download and uncompress.
	 * @param target The location where the zip file should be uncompressed. This
	 * will be created if it doesn't exist.
	 * @param status The Status object that provides a source of state change visiblity;
	 * can be null.
	 */
	public static void downloadZip(ParsedURL sourceZip, File targetFile, Status status)
	{
		ZipFile zipFile			=   null;
	   	Enumeration entries		=   null;
	   	
	   	//create the target parent directories if they don't exist
	   	if (!targetFile.getParentFile().exists())
	   		targetFile.getParentFile().mkdirs();
	   	   
	   	System.out.println("zip URL: " + sourceZip);
		try
		{    	           	            
			if (sourceZip.toString().startsWith("file://"))
			{
				String zipLoc = 
					sourceZip.toString().substring(7, sourceZip.toString().length());
				zipFile = new ZipFile(zipLoc);
			}
			else
			{
				ZipDownload zipDownload = new ZipDownload(sourceZip, targetFile, status);
				zipDownload.downloadAndWrite(true);
				//zipFile = new ZipFile(sourceZip.url().toString());
				return;
				
			}
	    	
			System.out.println("Extracting zip file: " + sourceZip);
			entries = zipFile.entries();
			while (entries.hasMoreElements())
			{
				ZipEntry entry = (ZipEntry) entries.nextElement();
				
				if (entry.isDirectory())
				{
					File entryDir = new File(targetFile.getParentFile(), entry.getName());
					if (!entryDir.exists())
						entryDir.mkdirs();
					
					continue;
				}
				File outFile = new File(targetFile.getParentFile(), entry.getName());
		        copyInputStream(zipFile.getInputStream(entry),
		           new BufferedOutputStream(new FileOutputStream(outFile)));
			}
			
			zipFile.close();
			
		}catch(IOException e)
		{
			System.out.println("Error, file not found on the server!");
			e.printStackTrace();
			return;
		}      
	}
	
	/**
	 * Tiny inner class to handle buffer I/O
	 * 
	 * @param in	The inputstream
	 * @param out	The outputstream
	 * @throws IOException	Throws IOException on invalid in or out stream.
	 */
	public static final void copyInputStream(InputStream in, OutputStream out)
	throws IOException
	{
	  byte[] buffer = new byte[1024];
	  int len;

	  while((len = in.read(buffer)) >= 0)
	    out.write(buffer, 0, len);

	  in.close();
	  out.close();
	}
	
	static final String interfaceSubDir	= Generic.parameter("userinterface");
	
	public static IIOPhoto getCachedIIOPhoto(String imagePath, DispatchTarget dispatchTarget)
	   {
		   boolean isCached = false;
		   IIOPhoto result		= null;
		   if (imagePath != null)
		   {
			   ParsedURL parsedPixelBasedURL	= null;
			   if(!imagePath.startsWith("file:///"))
			   {
				   File cachedFile = 
					   new File (Assets.getAsset(INTERFACE, Generic.parameter("userinterface")), 
						   						imagePath);
//				 used a cached copy if available
				   if (cachedFile.canRead())
				   {
					   System.out.println("Cache read: " + cachedFile);
					   parsedPixelBasedURL		= new ParsedURL(cachedFile);
					   isCached = true;
				   }
				   else
				   {
					   parsedPixelBasedURL = Generic.systemPhotoPath(imagePath);
				   }
				}
				else
				{
					parsedPixelBasedURL =  ParsedURL.getRelativeOrAbsolute(imagePath, 
							"getPixelBased()");
				}
			 if (parsedPixelBasedURL != null)
			 {
			    try
			    {
			    	result	= new IIOPhoto(parsedPixelBasedURL, dispatchTarget);
			    }
				catch (NullPointerException e)
			    {
			    	println("getPixelBased() ERROR: returning null.");
			    	e.printStackTrace();	
					return null;
			    }
		//	    result.checkForTimeout	= checkForTimeout;
		//	    debug("getPixelBased(download() "+pixelBasedString+") ->"+ pixelBasedURL);
			    result.download();
				if (!isCached) //cache the picture if it isn't already
				{
					result.cacheImage(
							Generic.parameter("userinterface") 	+
							File.separator						+
							imagePath);
				}
			 }
			 else
				 println("getCachedIIOPhoto( Couldn't find param "+imagePath);
	     }
		 return result;
	   }

}

