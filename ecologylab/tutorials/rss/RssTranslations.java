//RssTranslations.java

package ecologylab.tutorials.rss;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationScope;

public class RssTranslations extends Debug
{
   public static TranslationScope get()
   {
	   return TranslationScope.get("rss", RssState.class, Channel.class, Item.class);
   } 
}
