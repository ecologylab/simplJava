/**
 * 
 */
package ecologylab.tests;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_format;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.StringFormat;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class TestXMLFormat extends ElementState
{
	@simpl_scalar
	@simpl_format("#")
	double	decimal0	= 1.654654654654645321321;

	@simpl_scalar
	@simpl_format("#.#")
	double	decimal1	= -1.654654654654645321321;

	@simpl_scalar
	@simpl_format("#.0#")
	double	decimal2	= -11111.654654654654645321321;

	@simpl_scalar
	@simpl_format("#.###")
	double	decimal3	= 0.654654654654645321321;

	public TestXMLFormat()
	{

	}

	/**
	 * @param args
	 * @throws SIMPLTranslationException
	 */
	public static void main(String[] args) throws SIMPLTranslationException
	{
		TestXMLFormat t = new TestXMLFormat();

		SimplTypesScope.serialize(t, System.out, StringFormat.XML);

		System.out.println();

		SimplTypesScope translationScope = SimplTypesScope.get("test", TestXMLFormat.class);
		SimplTypesScope.serialize(translationScope.deserialize(SimplTypesScope.serialize(t,
				StringFormat.XML).toString(), StringFormat.XML), System.out, StringFormat.XML);
		System.out.println();

		System.out.println(SimplTypesScope.serialize(t, StringFormat.XML));
	}
}
