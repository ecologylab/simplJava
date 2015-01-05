package ecologylab.concurrent;

import ecologylab.logging.LogEvent;
import ecologylab.serialization.annotations.simpl_inherit;

/**
 * When a downloadable is enqueued.
 * 
 * @author quyin
 */
@simpl_inherit
public class EnqueueEvent extends LogEvent
{

  public EnqueueEvent()
  {
    super();
  }

}
