 //RssTranslations.java

package ecologylab.tutorials.rss;

import ecologylab.generic.Debug;
import ecologylab.serialization.SimplTypesScope;

public class RssTranslations extends Debug
{
   public static SimplTypesScope get()
   {
     return SimplTypesScope.get("rss", Rss.class, Channel.class, Item.class);
   } 
}