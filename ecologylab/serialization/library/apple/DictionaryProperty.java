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
@simpl_tag("dict")
@simpl_inherit
public class DictionaryProperty extends Property
{
	@simpl_collection
	@simpl_nowrap
	@simpl_classes(
	{ DictionaryProperty.class, StringProperty.class, KeyProperty.class, ArrayProperty.class,
			IntegerProperty.class, TrueProperty.class, FalseProperty.class, RealProperty.class, DataProperty.class })
	List<Property>	properties;

	public DictionaryProperty()
	{

	}
}
