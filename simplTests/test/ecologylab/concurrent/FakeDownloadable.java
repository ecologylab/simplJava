package ecologylab.concurrent;

import java.io.IOException;

import ecologylab.net.ParsedURL;

/**
 * For test.
 * 
 * @author quyin
 */
public class FakeDownloadable implements Downloadable
{

  ParsedURL             location;

  ParsedURL             downloadLocation;

  boolean               isImage;

  boolean               isCached;

  boolean               downloaded = false;

  DownloadableLogRecord logRecord  = new DownloadableLogRecord();

  public FakeDownloadable(ParsedURL location)
  {
    this(location, location, false, false);
  }

  public FakeDownloadable(ParsedURL location,
                          ParsedURL downloadLocation,
                          boolean isImage,
                          boolean isCached)
  {
    this.location = location;
    this.downloadLocation = downloadLocation;
    this.isImage = isImage;
  }

  @Override
  public void performDownload() throws IOException
  {
    downloaded = true;
  }

  @Override
  public void handleIoError(Throwable e)
  {
    throw new RuntimeException("I/O error while downloading", e);
  }

  @Override
  public boolean isRecycled()
  {
    return false;
  }

  @Override
  public void recycle()
  {
    // no-op
  }

  /**
   * Subclass can override this to return a fake site object.
   */
  @Override
  public Site getSite()
  {
    return null;
  }

  /**
   * Subclass can override this to return a fake site object.
   */
  @Override
  public Site getDownloadSite()
  {
    return null;
  }

  @Override
  public ParsedURL location()
  {
    return location;
  }

  @Override
  public ParsedURL getDownloadLocation()
  {
    return downloadLocation;
  }

  @Override
  public boolean isImage()
  {
    return isImage;
  }

  @Override
  public String message()
  {
    return getClass().getSimpleName() + "[" + location.toString() + "]";
  }

  @Override
  public boolean isCached()
  {
    return isCached;
  }

  @Override
  public DownloadableLogRecord getLogRecord()
  {
    return logRecord;
  }

}
