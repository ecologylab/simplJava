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
  private String  id;

  @simpl_composite
  private LogPost logPost;

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
  
  public LogPost logPost()
  {
    if (logPost == null)
    {
      synchronized (this)
      {
        if (logPost == null)
        {
          logPost = new LogPost();
        }
      }
    }
    return logPost;
  }

  public void addEnqueueEvent()
  {
    logPost().addEventNow(new EnqueueEvent());
  }

  public void addQueuePeekEvent()
  {
    logPost().addEventNow(new QueuePeekEvent());
  }

  public void addDownloadEvent()
  {
    logPost().addEventNow(new DownloadEvent());
  }

}
