/**
 * 
 */
package ecologylab.tests;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.formatenums.StringFormat;

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

	static final SimplTypesScope	TS	= SimplTypesScope.get("test_scalar", null,
																				TestScalarCollection.class);

	static final String						xml	= "<test_scalar_collection><person>fred</person><person>wilma</person><link>http://www.google.com</link><link>http://ecologylab.cs.tamu.edu</link></test_scalar_collection>";

	public static void main(String[] a)
	{
		try
		{
			Object es = TS.deserialize(xml, StringFormat.XML);
			SimplTypesScope.serialize(es, System.out, StringFormat.XML);
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}
	}
}
