/**
 * 
 */
package ecologylab.serialization.library.apple;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

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
}
