package cm.generic;

import java.io.*;

public interface Downloadable
{
/**
 * Called to start download.
 */
   public void performDownload()
      throws IOException;
/**
 * Called to inquire on the status of a download.<br>
 * @return	true if the download is complete.
 */
   public boolean isDownloadDone();
/**
 * Called in case a timeout happens.
 */
   public void handleTimeout();
/**
 * Called in case an IO error happens.
 */
   public void handleIoError();
}
