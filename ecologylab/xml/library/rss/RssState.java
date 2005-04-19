package ecologylab.xml.rss;

import ecologylab.xml.*;

public class RssState extends ElementState
{
   public float			version;
   
   public Channel		channel;

   public static void initTranslations()
   {
	  addTranslation("ecologylab.xml.rss", "RssState");
	  addTranslation("ecologylab.xml.rss", "Item");
	  addTranslation("ecologylab.xml.rss", "Channel");
   }
}
