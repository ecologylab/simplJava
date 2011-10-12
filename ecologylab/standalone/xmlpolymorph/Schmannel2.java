/**
 * 
 */
package ecologylab.standalone.xmlpolymorph;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.StringFormat;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_scope;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.library.rss.Item;
import ecologylab.serialization.library.rss.RssTranslations;

/**
 * @author andruid
 * 
 */
@simpl_tag("schmannel")
public class Schmannel2 extends ElementState
{
	private static final String						SCOPE_NAME				= "schm_rss";

	private static final SimplTypesScope	TRANSLATION_SCOPE	= SimplTypesScope.get(SCOPE_NAME,
																															RssTranslations.get(),
																															Schmannel2.class, SchmItem.class,
																															BItem.class);

	@simpl_scope(SCOPE_NAME)
	@simpl_collection
	ArrayList<Item>												schmItems;

	/**
	 * 
	 */
	public Schmannel2()
	{
		super();
	}

	public void polyAdd(Item item)
	{
		if (schmItems == null)
			schmItems = new ArrayList<Item>();
		schmItems.add(item);
	}

	public static final String	WRAP_OUT	= "<schmannel><schm_items><item></item></schm_items></schmannel>";																																																																																																																																					// "<channel><items></items></channel>";

	public static final String	ITEMS			= "<schmannel><schm_items><item><title>it is called rogue!</title><description>its a game</description><link>http://ecologylab.cs.tamu.edu/rogue/</link><author>zach</author></item><item><title>it is called cf!</title><description>its a creativity support tool</description><author>andruid</author></item></schm_items></schmannel>";

	public static void main(String[] s)
	{
		Item item = new Item("t2ec");
		Item schmItem = new SchmItem("cf");
		Item nested = new BItem("nested");

		Schmannel2 schmannel = new Schmannel2();

		schmannel.polyAdd(item);
		schmannel.polyAdd(schmItem);
		schmannel.polyAdd(nested);
		try
		{
			StringBuilder buffy = new StringBuilder();

			SimplTypesScope.serialize(schmannel, buffy, StringFormat.XML);

			System.out.println(buffy);
			System.out.println('\n');
			Object s2 = TRANSLATION_SCOPE.deserialize(buffy, StringFormat.XML);

			SimplTypesScope.serialize(s2, System.out, StringFormat.XML);
			// TRANSLATION_SCOPE.serialize(System.out);
			// System.out.println('\n');
		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
		testTranslateFrom();

	}

	protected static void testTranslateFrom()
	{
		try
		{
			Object rap = TRANSLATION_SCOPE.deserialize(ITEMS, StringFormat.XML);

			SimplTypesScope.serialize(rap, System.out, StringFormat.XML);
			System.out.println('\n');
			// println(c.translateToXML());
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}
	}

}
