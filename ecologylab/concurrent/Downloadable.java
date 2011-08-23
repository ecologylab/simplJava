package ecologylab.concurrent;

import java.io.IOException;

import ecologylab.io.DownloadProcessor;
import ecologylab.net.ParsedURL;

/**
 * Objects that implement this interface can be passed to a {@link DownloadProcessor DownloadProcessor}.
 *
 * @author andruid
 */
public interface Downloadable
{
/**
 * Called to start download.
 */
   public void performDownload()
      throws IOException;

/**
 * Called in case an IO error happens.
 * @param e TODO
 */
   public void handleIoError(Throwable e);
   
   /**
    * True if the Downloadable has been recycled, and thus should not be downloaded.
    * 
    * @return
    */
   public boolean isRecycled();
   
   public void recycle();
   
   public BasicSite getSite();
   public ParsedURL location();
   
   /**
    * 
    * @return	What to tell the user about what is being downloaded.
    */
   public String message();
}
