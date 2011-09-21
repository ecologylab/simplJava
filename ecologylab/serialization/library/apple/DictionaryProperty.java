/**
 * 
 */
package ecologylab.serialization.library.apple;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.deserializers.ISimplDeserializationPost;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@simpl_tag("dict")
@simpl_inherit
public class DictionaryProperty extends Property implements ISimplDeserializationPost
{
	@simpl_collection
	@simpl_nowrap
	@simpl_classes(
	{ DictionaryProperty.class, StringProperty.class, KeyProperty.class, ArrayProperty.class,
			IntegerProperty.class, TrueProperty.class, FalseProperty.class, RealProperty.class,
			DataProperty.class })
	List<Property>				properties;

	/**
	 * Not currently serialized; used for access only.
	 */
	Map<String, Property>	propertyMap;

	public DictionaryProperty()
	{

	}

	@Override
	public void deserializationPostHook(TranslationContext translationContext, Object object)
	{
		propertyMap = new HashMap<String, Property>();

		Iterator<Property> propertyIter = properties.iterator();

		while (propertyIter.hasNext())
		{
			KeyProperty key = (KeyProperty) propertyIter.next();
			Property value = propertyIter.next();

			propertyMap.put(key.getContents(), value);
		}
	}

	public Property getProperty(String key)
	{
		return this.propertyMap.get(key);
	}
}
