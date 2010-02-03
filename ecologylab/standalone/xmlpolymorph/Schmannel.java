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
public class Schmannel extends ElementState
{
  private static final TranslationScope	
  TRANSLATION_SPACE	= TranslationScope.get("schm_rss", RssTranslations.get(), Schmannel.class, SchmItem.class, BItem.class);

  @xml_classes({Item.class, SchmItem.class})
  @xml_collection ArrayList<Item> schmItems;

  @xml_classes({BItem.class})
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
		Item nested	= new BItem("nested");
		
		Schmannel schmannel	= new Schmannel();
		
		schmannel.polyItem	= nested;
		
		schmannel.polyAdd(item);
		schmannel.polyAdd(schmItem);
		
		try
		{
			StringBuilder buffy	= new StringBuilder();
			schmannel.translateToXML(buffy);
			
			System.out.println(buffy);
			System.out.println('\n');
			TRANSLATION_SPACE.translateToXML(System.out);
			System.out.println('\n');
			ElementState s2	= ElementState.translateFromXMLCharSequence(buffy, TRANSLATION_SPACE);
			s2.translateToXML(System.out);
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
