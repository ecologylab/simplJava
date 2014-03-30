package ecologylab.concurrent;

import ecologylab.logging.LogEvent;
import ecologylab.serialization.annotations.simpl_inherit;

/**
 * When a Downloadable is downloaded.
 * 
 * @author quyin
 */
@simpl_inherit
public class DownloadEvent extends LogEvent
{

  public DownloadEvent()
  {
    super();
  }

}
