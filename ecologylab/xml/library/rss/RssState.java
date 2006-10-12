package ecologylab.xml.rss;

import ecologylab.xml.*;

public class RssState extends ElementState
{
	@xml_attribute	public	float		version;
   
	@xml_nested		public	Channel		channel;
}
