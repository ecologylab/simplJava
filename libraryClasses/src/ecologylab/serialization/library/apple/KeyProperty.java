/**
 * 
 */
package ecologylab.serialization.library.apple;

import java.io.File;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_tag;
import simpl.exceptions.SIMPLTranslationException;

import ecologylab.generic.Debug;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.formatenums.StringFormat;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@simpl_tag("key")
@simpl_inherit
public class KeyProperty extends StringProperty
{

	/**
	 * 
	 */
	public KeyProperty()
	{
	}

	public KeyProperty(String string)
	{
		this.contents = string;
	}

	@Override
	public String toString()
	{
		return "KeyProperty: " + contents;
	}

	public static void main(String[] args) throws SIMPLTranslationException
	{
		SimplTypesScope key = PListTranslations.getKey();
		StringProperty sample = (StringProperty) key.deserialize(	new File("/Users/toupsz/Documents/workspace/ecologylabFundamental/ecologylab/serialization/library/apple/keyTest.xml"),
																															Format.XML);

		Debug.println("sample: " + sample.toString());

		KeyProperty newKey = new KeyProperty("hi");
		Debug.println("newKey: " + SimplTypesScope.serialize(newKey, StringFormat.XML));

		StringProperty newString = new StringProperty("hi");
		Debug.println("newString: " + SimplTypesScope.serialize(newString, StringFormat.XML));
	}
}
