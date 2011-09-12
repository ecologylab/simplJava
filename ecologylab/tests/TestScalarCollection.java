/**
 * 
 */
package ecologylab.tests;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.StringFormat;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;

/**
 * 
 * @author andruid
 */
public class TestScalarCollection extends ElementState
{
	@simpl_nowrap
	@simpl_collection("person")
	ArrayList<String>							stuff;

	@simpl_nowrap
	@simpl_collection("link")
	ArrayList<ParsedURL>					purls;

	static final TranslationScope	TS	= TranslationScope.get("test_scalar", null,
																				TestScalarCollection.class);

	static final String						xml	= "<test_scalar_collection><person>fred</person><person>wilma</person><link>http://www.google.com</link><link>http://ecologylab.cs.tamu.edu</link></test_scalar_collection>";

	public static void main(String[] a)
	{
		try
		{
			Object es = TS.deserialize(xml, StringFormat.XML);
			ClassDescriptor.serialize(es, System.out, StringFormat.XML);
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}
	}
}
