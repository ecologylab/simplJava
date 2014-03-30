package ecologylab.concurrent;

import ecologylab.logging.LogEvent;
import ecologylab.serialization.annotations.simpl_inherit;

/**
 * When a downloadable is peeked while it is at the head of the download queue.
 * 
 * @author quyin
 */
@simpl_inherit
public class QueuePeekEvent extends LogEvent
{

  public QueuePeekEvent()
  {
    super();
  }

}
