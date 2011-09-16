/**
 * 
 */
package ecologylab.serialization.library.apple;

import java.util.List;

import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@simpl_inherit
@simpl_tag("array")
public class ArrayProperty extends Property
{
	@simpl_collection
	@simpl_nowrap
	@simpl_classes(
	{ DictionaryProperty.class, StringProperty.class, ArrayProperty.class, RealProperty.class })
	List<Property>	dictionaries;

	/**
	 * 
	 */
	public ArrayProperty()
	{
		// TODO Auto-generated constructor stub
	}

}
