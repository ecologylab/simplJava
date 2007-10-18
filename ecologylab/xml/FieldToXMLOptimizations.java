/**
 * 
 */
package ecologylab.xml;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import ecologylab.generic.Debug;
import ecologylab.generic.ReflectionTools;
import ecologylab.xml.types.scalar.ScalarType;
import ecologylab.xml.types.scalar.TypeRegistry;

/**
 * Small data structure used to optimize translation to XML.
 * 
 * Uses Class or Field so that it can dynamically get custom tags.
 * 
 * @author andruid
 */
class TagMapEntry
extends Debug
implements ParseTableEntryTypes
{
    private 	String 		startOpenTag;

    private		String 		closeTag;
    
    private		String		tagName;
    
    private int				type;
    
    private boolean			isCDATA;
    
    private boolean			needsEscaping;
    
    private ScalarType		scalarType;
    
    
    //TODO -- change to take Field instead of type!!!!!!
    TagMapEntry(Class<? extends ElementState> classObj, boolean compression, int type)
    {
        setTag(classObj.isAnnotationPresent(ElementState.xml_tag.class) ? classObj.getAnnotation(
                ElementState.xml_tag.class).value() : XmlTools.getXmlTagName(classObj, "State", compression));
        this.type			= type;
    }

    TagMapEntry(Field field, boolean compression)
    {
    	final ElementState.xml_collection collectionAnnotationObj	= field.getAnnotation(ElementState.xml_collection.class);
    	final String collectionAnnotation	= (collectionAnnotationObj == null) ? null : collectionAnnotationObj.value();
    	final ElementState.xml_map mapAnnotationObj	= field.getAnnotation(ElementState.xml_map.class);
    	final String mapAnnotation	= (mapAnnotationObj == null) ? null : mapAnnotationObj.value();
    	final ElementState.xml_tag tagAnnotationObj					= field.getAnnotation(ElementState.xml_tag.class);
    	final String tagAnnotation			= (tagAnnotationObj == null) ? null : tagAnnotationObj.value();
    	final String tagName	= (collectionAnnotation != null) ? collectionAnnotation :
    							  (mapAnnotation != null) ? mapAnnotation :
    							  (tagAnnotation != null) ? tagAnnotation :
    							   XmlTools.getXmlTagName(field.getName(), null, compression); // generate from class name
        setTag(tagName);
        type				= getType(field);
        boolean isLeaf		= (type == LEAF_NODE_VALUE);
        if (isLeaf || (type == REGULAR_ATTRIBUTE))
        {
        	scalarType		= TypeRegistry.getType(field);
        	if (isLeaf)
        	{
	        	isCDATA			= XmlTools.leafIsCDATA(field);
	        	needsEscaping	= scalarType.needsEscaping();
        	}
        }
    }

    @Override public String toString()
    {
        return "TagMapEntry" + closeTag;
    }
    
    private void setTag(String tagName)
    {
    	this.tagName		= tagName;
        startOpenTag 		= "<" + tagName;
        closeTag			= "</" + tagName + ">";
    }
	
	static int getType(Field field)
	{
		int	result			= UNSET_TYPE;
		if (field.isAnnotationPresent(ElementState.xml_attribute.class))
		{
			result			= REGULAR_ATTRIBUTE;
		}
		else if (field.isAnnotationPresent(ElementState.xml_leaf.class))
		{
			result			= LEAF_NODE_VALUE;
		}
		else if (field.isAnnotationPresent(ElementState.xml_nested.class))
			result			= REGULAR_NESTED_ELEMENT;
		else if (field.isAnnotationPresent(ElementState.xml_collection.class))
		{
			java.lang.reflect.Type[] typeArgs	= ReflectionTools.getParameterizedTypeTokens(field);
			if (typeArgs != null)
			{
				final Type typeArg0				= typeArgs[0];
				if (typeArg0 instanceof Class)
				{	// generic variable is assigned in declaration -- not a field in an ArrayListState or some such
					Class	collectionElementsType	= (Class) typeArg0;
					println("TagMapEntry: !!!collection elements are of type: " + collectionElementsType.getName());
					// is collectionElementsType a scalar or a nested element
					if (ElementState.class.isAssignableFrom(collectionElementsType))
					{	// nested element
						result						= COLLECTION_ELEMENT;						
					}
					else
					{	// scalar
						result						= COLLECTION_SCALAR;
					}
				}
				else	//FIXME -- assume that if 
					result							= COLLECTION_ELEMENT;						
					
			}
			else
			{
				println("WARNING: Cant translate  @xml_collection(\"" + field.getName() + 
						  	 "\") because it is not annotating a parameterized generic Collection defined with a <type> token.");
				result						= IGNORED_ELEMENT;
			}
		}
		else if (field.isAnnotationPresent(ElementState.xml_map.class))
		{
			java.lang.reflect.Type[] typeArgs	= ReflectionTools.getParameterizedTypeTokens(field);
			if (typeArgs != null)
			{
				final Type typeArg0				= typeArgs[1];	// 2nd generic type arg -- type of values, not keys
				if (typeArg0 instanceof Class)
				{	// generic variable is assigned in declaration -- not a field in an ArrayListState or some such

					Class	mapElementsType			= (Class) typeArg0;
					println("TagMapEntry: !!!map elements are of type: " + mapElementsType.getName());
					if (ElementState.class.isAssignableFrom(mapElementsType))
					{	// nested element
						result						= MAP_ELEMENT;						
					}
					else
					{	// scalar
						result						= MAP_SCALAR;
					}
				}
				else	//FIXME -- assume that if 
					result							= MAP_ELEMENT;						
			}
			else
			{
				println("WARNING: Cant translate  @xml_map(\"" + field.getName() + 
						  	 "\") because it is not annotating a parameterized generic Collection defined with a <type> token.");
				result						= IGNORED_ELEMENT;
			}
		}
		return result;
	}

	public int type()
	{
		return type;
	}

	public boolean isCDATA()
	{
		return isCDATA;
	}

	public boolean isNeedsEscaping()
	{
		return needsEscaping;
	}

	public String closeTag()
	{
		return closeTag;
	}

	public String tagName()
	{
		return tagName;
	}
	public String startOpenTag()
	{
		return startOpenTag;
	}
}