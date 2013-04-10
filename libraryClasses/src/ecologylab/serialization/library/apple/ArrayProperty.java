/**
 * 
 */
package ecologylab.serialization.library.apple;

import java.util.List;

import simpl.annotations.dbal.simpl_classes;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_tag;


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

	public List<Property> getPropertyList()
	{
		return this.dictionaries;
	}
}
