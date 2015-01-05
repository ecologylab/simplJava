/**
 * 
 */
package ecologylab.tests;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.formatenums.StringFormat;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
@simpl_tag("fred:flintstone")
public class Composed extends ElementState
{
	@simpl_collection("ClassTagged")
	ArrayList<ClassTagged>	tagged	= new ArrayList<ClassTagged>();

	@simpl_scalar
	int											x				= 22;

	/**
     * 
     */
	public Composed()
	{
		tagged.add(new ClassTagged());
		tagged.add(new ClassTagged());
		tagged.add(new ClassTagged());
	}

	static final Class[]	classes	=
																{ Composed.class, ClassTagged.class, FieldTagged.class };

	public static void main(String[] args) throws SIMPLTranslationException
	{
		SimplTypesScope ts = SimplTypesScope.get("testXMLTag", classes);

		Composed c = new Composed();

		final StringBuilder translatedXML = SimplTypesScope.serialize(c, StringFormat.XML);

		System.out.println(translatedXML);

		Composed retranslated = (Composed) ts.deserialize(translatedXML, StringFormat.XML);
		// Composed retranslated = (Composed) ElementState.translateFromXMLSAX(translatedXML, ts);

		SimplTypesScope.serialize(c, System.out, StringFormat.XML);
		System.out.println("\n\nretranslated:");

		SimplTypesScope.serialize(retranslated, System.out, StringFormat.XML);
		// retranslated.translateToXML(System.out);
	}

}
