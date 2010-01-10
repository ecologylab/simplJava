/**
 * 
 */
package ecologylab.standalone.xmlpolymorph;

import java.util.ArrayList;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.ElementState.xml_collection;
import ecologylab.xml.library.rss.Channel;
import ecologylab.xml.library.rss.Item;
import ecologylab.xml.library.rss.RssTranslations;

/**
 * @author andruid
 *
 */
public class Schmannel extends Channel
{
  private static final TranslationScope	
  TRANSLATION_SPACE	= TranslationScope.get("schm_rss",RssTranslations.get(), Schmannel.class, SchmItem.class);

  @xml_classes({Item.class, SchmItem.class})
  @xml_collection("item") ArrayList<Item> schmItems;

  @xml_classes({Item.class, SchmItem.class})
  @xml_nested 							Item 						polyItem;
  
	/**
	 * 
	 */
	public Schmannel()
	{
		super();
	}
	public void polyAdd(Item item)
	{
		if (schmItems == null)
			schmItems	= new ArrayList<Item>();
		schmItems.add(item);
	}

	public static final String WRAP_OUT = "<schmannel><schm_items><item></item></schm_items></schmannel>"; // "<channel><items></items></channel>";
	public static final String ITEMS = "<schmannel><schm_items><item><title>it is called rogue!</title><description>its a game</description><link>http://ecologylab.cs.tamu.edu/rogue/</link><author>zach</author></item><item><title>it is called cf!</title><description>its a creativity support tool</description><author>andruid</author></item></schm_items></schmannel>";
	public static void main(String[] s)
	{
		Item item	= new Item("t2ec");
		Item schmItem	= new SchmItem("cf");
		
		Schmannel schmannel	= new Schmannel();
		schmannel.polyItem	= schmItem;
		
		schmannel.polyAdd(item);
		schmannel.polyAdd(schmItem);
		
		try
		{
			schmannel.translateToXML(System.out);
		}
		catch (XMLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		testTranslateFrom();

	}
	protected static void testTranslateFrom()
	{
		try
		{
			ElementState rap	= ElementState.translateFromXMLCharSequence(ITEMS, TRANSLATION_SPACE);
			rap.translateToXML(System.out);
			System.out.println('\n');
//			println(c.translateToXML());
		} catch (XMLTranslationException e)
		{
			e.printStackTrace();
		}
	}

}
