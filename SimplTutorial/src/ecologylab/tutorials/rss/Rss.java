 //Rss.java

package ecologylab.tutorials.rss;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.library.rss.Channel;

public class Rss extends ElementState
{
  @simpl_scalar  float    version;
  @simpl_composite    Channel    channel;

  public Rss() {}
}