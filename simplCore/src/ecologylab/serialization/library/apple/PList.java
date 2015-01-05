/**
 * 
 */
package ecologylab.serialization.library.apple;

import java.io.File;

import ecologylab.generic.Debug;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.formatenums.Format;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@simpl_tag("plist")
public class PList extends ElementState
{
	@simpl_scalar
	double							version;

	@simpl_composite
	DictionaryProperty	dict;

	public PList()
	{

	}

	public DictionaryProperty getDict()
	{
		return dict;
	}

	public static void main(String[] args) throws SIMPLTranslationException
	{
		PList sample = (PList) PListTranslations.get()
																						.deserialize(	new File("/Users/toupsz/Documents/workspace/ecologylabFundamental/ecologylab/serialization/library/apple/plist.xml"),
																													Format.XML);
		
		for (Property p : sample.getDict().propertyMap.values())
		{
			Debug.println(p.toString());
		}
		
		for (Property p : sample.getDict().properties)
		{
			Debug.println(p.toString());
		}
	}
}
