package cm.generic;

import java.io.*;

public interface Downloadable
{
/**
 * Called to start download.
 */
   public void performDownload()
      throws Exception;
/**
 * Called to inquire on the status of a download.<br>
 * @return	true if the download is complete.
 */
   public boolean isDownloadDone();
/**
 * Called in case a timeout happens.
 * 
 * @return	true, if the object is able to abort the download and
 * release resources.
 * @return	false if its stuck.
 */
   public boolean handleTimeout();
/**
 * Called in case an IO error happens.
 */
   public void handleIoError();
}
