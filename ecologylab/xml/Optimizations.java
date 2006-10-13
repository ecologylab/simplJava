package ecologylab.xml;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
	 * Class object that we are holding optimizations for.
	 */
	Class							thatClass;
	/**
	 * A map of all the Optimizations objects.
	 * The keys are simple class names.
	 */
	private static final HashMap	registry	= new HashMap();
		
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
	
	/**
	 * The fields that are represented as attributes for the class we're optimizing.
	 */
	private ArrayList				attributeFields;
	
	/**
	 * The fields that are represented as nested elements (including leaf nodes)
	 * for the class we're optimizing.
	 */
	private ArrayList				elementFields;
	
	
	private Optimizations(Class thatClass)
	{
		super();
		this.thatClass		= thatClass;
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
		String className	= getClassName(thatClass);
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
					result				= new Optimizations(thatClass);
					registry.put(className, result);
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
	
	/**
	 * Get the fields that are represented as attributes for the class we're optimizing.
	 * Uses lazy evaluation -- while derive the answer for attributefieldbs and elementFields, and cache it.
	 * 
	 * @return	ArrayList of Field objects.
	 */
	ArrayList attributeFields()
	{
		ArrayList result	= attributeFields;
		if (result == null)
		{
			getAndOrganizeFields();
			result			= attributeFields;
		}
		return result;
	}
	/**
	 * Get the fields that are represented as nested elements (including leaf nodes)
	 * for the class we're optimizing.
	 * Uses lazy evaluation -- while derive the answer for attributefieldbs and elementFields, and cache it.
	 * 
	 * @return	ArrayList of Field objects.
	 */
	ArrayList elementFields()
	{
		ArrayList result	= elementFields;
		if (result == null)
		{
			getAndOrganizeFields();
			result			= elementFields;
		}
		return result;
	}
	/**
	 * Performs the lazy evaluation to get attribute and element field collections.
	 * Dispatches to the correct routine for this, based on ElementState.declarationStyle().
	 *
	 */
	private void getAndOrganizeFields()
	{
		attributeFields		= new ArrayList();
		elementFields		= new ArrayList();
		ElementState.DeclarationStyle ds = ElementState.declarationStyle();
		switch (ds)
		{
		case PUBLIC:
			getAndOrganizeFieldsPublic(thatClass, attributeFields, elementFields);
			break;
		case ANNOTATION:
			getAndOrganizeFieldsRecursive(thatClass, attributeFields, elementFields);
			break;
		default:
			throw new RuntimeException("Unsupported declaration style: " + ds);
		}
	}
	/**
	 * Performs the lazy evaluation to get attribute and element field collections.
	 * Uses the hip new annotation stylee.
	 * Uses the old PUBLIC declaration stylee.
	 */
	private static void getAndOrganizeFieldsPublic(Class thatClass,
			ArrayList attributeFields, ArrayList elementFields)
	{
		Field[] fields		= thatClass.getFields();
			
		for (int i = 0; i < fields.length; i++)
		{
			Field thatField	= fields[i];
			int fieldModifiers		= thatField.getModifiers();

			// skip static fields, since we're saving instances,
			// and inclusion w each instance would be redundant.
			if ((fieldModifiers & Modifier.STATIC) == Modifier.STATIC)
			{
//				debug("Skipping " + thatField + " because its static!");
				continue;
			 }
			if (XmlTools.isScalarValue(thatField) && !XmlTools.representAsLeafNode(thatField))
			{
				attributeFields.add(thatField);
			}
			else
			{
				elementFields.add(thatField);
			}
		}
	}
	/**
	 * Performs the lazy evaluation to get attribute and element field collections.
	 * Uses the hip new annotation declaration stylee.
	 * Recurses up the chain of inherited Java classes, if @xml_inherit is specified.
	 */
	private static void getAndOrganizeFieldsRecursive(Class thatClass,
			ArrayList attributeFields, ArrayList elementFields)
	{
		Field[] fields		= thatClass.getDeclaredFields();
			
		for (int i = 0; i < fields.length; i++)
		{
			Field thatField	= fields[i];
			int fieldModifiers		= thatField.getModifiers();

			// skip static fields, since we're saving instances,
			// and inclusion w each instance would be redundant.
			if ((fieldModifiers & Modifier.STATIC) == Modifier.STATIC)
			{
//				debug("Skipping " + thatField + " because its static!");
				continue;
			 }
			if (XmlTools.representAsAttribute(thatField))
			{
				attributeFields.add(thatField);
			}
			else if (XmlTools.representAsLeafOrNested(thatField))
			{
				elementFields.add(thatField);
			}
			// else -- ignore non-annotated fields
		}
		if (thatClass.isAnnotationPresent(xml_inherit.class)) 
		{	// recurse on super class
			Class superClass	= thatClass.getSuperclass();
			if (superClass != null)
				getAndOrganizeFieldsRecursive(superClass, attributeFields, elementFields);
		}
			
	}	
}

