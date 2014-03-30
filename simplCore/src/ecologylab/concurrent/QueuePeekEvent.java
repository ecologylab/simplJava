package ecologylab.concurrent;

import ecologylab.logging.LogEvent;

/**
 * When a downloadable is peeked while it is at the head of the download queue.
 * 
 * @author quyin
 */
public class QueuePeekEvent extends LogEvent
{

  public QueuePeekEvent()
  {
    super();
  }

}
