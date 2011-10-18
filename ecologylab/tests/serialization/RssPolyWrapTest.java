/**
 * 
 */
package ecologylab.tests.serialization;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.formatenums.StringFormat;

/**
 * @author andruid
 * 
 */
@simpl_tag("rss")
public class RssPolyWrapTest extends Base
{
	@simpl_composite
	ChannelTest														channel;

	public static final String						POLY_WRAP_TEST_TRANSLATIONS_NAME	= "poly_wrap_test_translations";

	public static final SimplTypesScope	POLY_WRAP_TEST_TRANSLATIONS				= SimplTypesScope
																																							.get(
																																									POLY_WRAP_TEST_TRANSLATIONS_NAME,
																																									Base.BASE_TRANSLATIONS,
																																									RssPolyWrapTest.class,
																																									ChannelTest.class,
																																									ItemTest.class);

	public static final ParsedURL					BUZZ_SPORTS												= ParsedURL
																																							.getAbsolute("http://buzzlog.yahoo.com/feeds/buzzsportm.xml");

	/**
	 * 
	 */
	public RssPolyWrapTest()
	{

	}

	public static void main(String[] args)
	{
		try
		{
			Object result = POLY_WRAP_TEST_TRANSLATIONS.deserialize(BUZZ_SPORTS, Format.XML);
			System.out.println('\n');
			SimplTypesScope.serialize(result, System.out, StringFormat.XML);

			System.out.println('\n');
		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\n");
	}

}
