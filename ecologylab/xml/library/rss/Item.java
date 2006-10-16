package ecologylab.xml.rss;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.dc.Dc;
import ecologylab.xml.media.Media;

/**
 * RSS 1 item element declaration.
 *
 * @author andruid
 */
public class Item extends ElementState
{
   @xml_leaf	public String			title;
   @xml_leaf	public String			description;
   @xml_leaf	public ParsedURL		link;
   @xml_leaf	public String			author;
   
   /**
    * Some people put Dublin Core fields into their items. Go figure :-)
    */
   @xml_nested	public Dc			dc;
   
   @xml_nested	public Media		media;
}
