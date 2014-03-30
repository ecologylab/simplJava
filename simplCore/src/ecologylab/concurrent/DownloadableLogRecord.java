package ecologylab.concurrent;

import ecologylab.logging.LogPost;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * Logging information for a Downloadable.
 * 
 * @author ajit
 * @author quyin
 */
public class DownloadableLogRecord
{

  @simpl_scalar
  private String      id;

  @simpl_composite
  private LogPost logPost;

  @simpl_scalar
  private boolean     htmlCacheHit;

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public LogPost getLogPost()
  {
    return logPost;
  }

  public void addEnqueueEvent()
  {
    getLogPost().addEventNow(new EnqueueEvent());
  }

  public void addQueuePeekEvent()
  {
    getLogPost().addEvent(new QueuePeekEvent());
  }

  public boolean isHtmlCacheHit()
  {
    return htmlCacheHit;
  }

  public void setHtmlCacheHit(boolean bHTMLCacheHit)
  {
    this.htmlCacheHit = bHTMLCacheHit;
  }

}
