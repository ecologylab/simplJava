package ecologylab.xml;

import java.util.HashMap;

import org.w3c.dom.Node;

import ecologylab.generic.Debug;
import ecologylab.generic.Generic;

/**
 * Cached object that holds all of the structures needed to optimize
 * translations to and from XML for a single subclass of ElementState.
 * A registry keeps track of these, using the XML tag as the key.
 * <p/>
 * This structure itself, as well as the structure within it, are created just in time,
 * by lazy evaluation.
 *
 * @author andruid
 */
class Optimizations extends Debug
{
	/**
	 * A map of all the Optimizations objects.
	 * The keys are simple class names.
	 */
	private static final HashMap	registry	= new HashMap();
	
	/**
	 * Holds the declarations of leaf element field names.
	 */
	private HashMap					leafElementFields;
	
	/**
	 * Found before the colon while translating from;
	 * emitted with a colon while translating to.
	 */
	private String					nameSpacePrefix;
	
	/**
	 * Used to optimize translateToXML().
	 */
	private HashMap					fieldNameOrClassToTagMap	= new HashMap();
	
	/**
	 * Map of ParseTableEntrys. The keys are field names.
	 * Used to optimize translateFromXML(...).
	 */
	private HashMap					parseTable					= new HashMap();
	
	
	private Optimizations()
	{
		super();		
	}
	private Optimizations(String[] leafElementFieldNames)
	{
		this();
		registerLeafElementFields(leafElementFieldNames);
	}
	
	/**
	 * The only mechanism for obtaining an Optimizations object, since the constructor is private.
	 * Uses just-in-time / lazy evaluation.
	 * The first time this is called for a given ElementState class, it constructs a new Optimizations
	 * saves it in our registry, and returns it.
	 * <p/>
	 * Subsequent calls merely pass back the already created object from the registry.
	 * 
	 * @param elementState
	 * @return
	 */
	static Optimizations lookup(ElementState elementState)
	{
		Class thatClass		= elementState.getClass();
		String className	= thatClass.getSimpleName();
		// stay out of the synchronized block most of the time
		Optimizations result = (Optimizations) registry.get(className);
		if (result == null)
		{
			// but still be thread safe!
			synchronized (registry)
			{
				result = (Optimizations) registry.get(className);
				if (result == null)
				{
					result				= new Optimizations(elementState.leafElementFieldNames());
					registry.put(className, result);
				}
			}
		}
		return result;
	}
	
	private void registerLeafElementFields(String[] leafElementFieldNames)
	{
		if (leafElementFieldNames != null)
			leafElementFields	= Generic.buildHashMapFromStrings(leafElementFieldNames);
	}
	boolean isLeafElementField(String fieldName)
   	{
   		return (leafElementFields != null) && leafElementFields.containsKey(fieldName);
   	}
	/**
	 * Get a tag translation object that corresponds to the fieldName,
	 * with this class. If necessary, form that tag translation object,
	 * and cache it.
	 */
	TagMapEntry getTagMapEntry(Class thatClass, boolean compression)
	{
		TagMapEntry result= (TagMapEntry)fieldNameOrClassToTagMap.get(thatClass);
		if (result == null)
		{
			synchronized (fieldNameOrClassToTagMap)
			{
				result		= (TagMapEntry) fieldNameOrClassToTagMap.get(thatClass);
				if (result == null)
				{
					String tagName	= XmlTools.getXmlTagName(thatClass, "State", compression);
					result	= new TagMapEntry(tagName);
					fieldNameOrClassToTagMap.put(thatClass, result);
					//debug(tagName.toString());
				}
			}
		}
		return result;
	}
	/**
	 * Get a tag translation object that corresponds to the fieldName,
	 * with this class. If necessary, form that tag translation object,
	 * and cache it.
	 */
	TagMapEntry getTagMapEntry(String fieldName, boolean compression)
	{
		TagMapEntry result= (TagMapEntry)fieldNameOrClassToTagMap.get(fieldName);
		if (result == null)
		{
			synchronized (fieldNameOrClassToTagMap)
			{
				result		= (TagMapEntry) fieldNameOrClassToTagMap.get(fieldName);
				if (result == null)
				{
					String tagName	= XmlTools.getXmlTagName(fieldName, "State", compression);
					result	= new TagMapEntry(tagName);
//					debug(tagName.toString());
					fieldNameOrClassToTagMap.put(fieldName, result);
				}
			}
		}
		return result;
	}
	/**
	 * @return Returns the nameSpacePrefix.
	 */
	String nameSpacePrefix()
	{
		return nameSpacePrefix;
	}
	/**
	 * @param nameSpacePrefix The nameSpacePrefix to set.
	 */
	void setNameSpacePrefix(String nameSpacePrefix)
	{
		this.nameSpacePrefix = nameSpacePrefix;
	}

	/**
	 * Get an object that describes how the field gets translated from XML.
	 * We cache these in the parseTable for each class, to optimize speed.
	 * <p/>
	 * These entries are created as needed, just-in-time, by lazy evaluation.
	 * @param field TODO
	 * @param translationSpace TODO
	 * @param context TODO
	 * @param node TODO
	 */
	ParseTableEntry parseTableEntry(TranslationSpace translationSpace, ElementState context, Node node)
	{
		String tag				= node.getNodeName();
		return parseTableEntry(translationSpace, context, tag);
	}
	ParseTableEntry parseTableEntry(TranslationSpace translationSpace, ElementState context, String tag)
	{
		ParseTableEntry result	= (ParseTableEntry) parseTable.get(tag);
		
		if (result == null)
		{
			result				= new ParseTableEntry(translationSpace, this, context, tag, false);
			parseTable.put(tag, result);
		}
		return result;
	}
	ParseTableEntry parseTableAttrEntry(TranslationSpace translationSpace, ElementState context, Node node)
	{
		String tag				= node.getNodeName();
		return parseTableAttrEntry(translationSpace, context, tag);
	}
	ParseTableEntry parseTableAttrEntry(TranslationSpace translationSpace, ElementState context, String tag)
	{
		ParseTableEntry result	= (ParseTableEntry) parseTable.get(tag);
		
		if (result == null)
		{
			result				= new ParseTableEntry(translationSpace, this, context, tag, true);
			parseTable.put(tag, result);
		}
		return result;
	}
}
