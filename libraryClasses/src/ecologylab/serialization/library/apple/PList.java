/**
 * 
 */
package ecologylab.serialization.library.apple;

import java.io.File;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;
import simpl.core.ElementState;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;

import ecologylab.generic.Debug;

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
