package ecologylab.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import ecologylab.generic.Debug;
import ecologylab.generic.HashMapFromStringsWriteSynch;
import ecologylab.generic.HashMapWriteSynch;
import ecologylab.generic.HashMapWriteSynch3;
import ecologylab.generic.ValueFactory;
import ecologylab.xml.ElementState.xml_tag;

/**
 * Cached object that holds all of the structures needed to optimize
 * translations to and from XML for a single subclass of ElementState.
 * A rootOptimizationsMap keeps track of these, using the XML tag as the key.
 * <p/>
 * This structure itself, as well as the structure within it, are created just in time,
 * by lazy evaluation.
 *
 * @author andruid
 */
public class Optimizations extends Debug
implements OptimizationTypes
{
	/**
	 * Class object that we are holding optimizations for.
	 */
	Class							thatClass;
	/**
	 * A map of root level Optimizations objects.
	 * The keys are simple class names.
	 */
	//TODO -- replace with OptimizationsMap
	private static final HashMap<String, Optimizations>	rootOptimizationsMap	= new HashMap<String, Optimizations>();
	
	/**
	 * Map of Optimizations objects for (the classes of) our children.
	 * We need to collect these in this scope, because there could be different metalanguage declarations.
	 * (IS THIS TRUE? EVEN I AM NOT SURE. -- ANDRUID 11/2/07)
	 */
	private final OptimizationsMap						childOptimizationsMap	= new OptimizationsMap();
	
	/**
	 * Keys are URNs (not NameSpaceIdentifiers!).
	 * 
	 * Values are ElementState subclasses.
	 */
	private static final HashMap<String, Class<? extends ElementState>>	nameSpaceRegistryByURN = new HashMap<String, Class<? extends ElementState>>();
		
	/**
	 * Found before the colon while translating from;
	 * emitted with a colon while translating to.
	 */
	private String										nameSpacePrefix;
	
	/**
	 * Map of FieldToXMLOptimizations, with field names as keys.
	 * 
	 * Used to optimize translateToXML().
	 */
	private HashMap<Object, FieldToXMLOptimizations>	fieldToXMLOptimizationsMap	= new HashMap<Object, FieldToXMLOptimizations>();
	
	/**
	 * Map of NodeToJavaOptimizations. The keys are tag names.
	 * Used to optimize translateFromXML(...).
	 */
	private HashMap<String, NodeToJavaOptimizations>	nodeToJavaOptimizationsMap	= new HashMap<String, NodeToJavaOptimizations>();
	
	private HashMap<String, Class<? extends ElementState>>	nameSpacesByID	= new HashMap<String, Class<? extends ElementState>>();
	
	/**
	 * The fields that are represented as attributes for the class we're optimizing.
	 */
	private ArrayList<Field>					attributeFields;
	
	private ArrayList<FieldToXMLOptimizations>	attributeFieldOptimizations;
	
	/**
	 * The fields that are represented as nested elements (including leaf nodes)
	 * for the class we're optimizing.
	 */
	private ArrayList<Field>					elementFields;
	
	private ArrayList<FieldToXMLOptimizations>	elementFieldOptimizations;
	
	/**
	 * Map of Fields, with field names as keys.
	 */
	//TODO -- couldn't we just use the fieldToXMLOptimizationsMap to do these lookups, and thus drop this!?
	private HashMap<String, Field>				fieldsMap;
	
	/**
	 * Used to see if there are any Map type objects, during the formation of NodeToJavaOptimizations,
	 * in order to match with declarations of @xml_map.
	 */
	private HashMap<String, Field>				mapFieldsByTag;
	
	/**
	 * Used to see if there are any Collection type objects, during the formation of NodeToJavaOptimizations,
	 * in order to match with declarations of @xml_collection.
	 */
	private HashMap<String, Field>				collectionFieldsByTag;
	
	
	/**
	 * Constructor is private, because values of this type are accessed through lazy evaluation, and cached.
	 * See also lookupRoot(), lookupChildOptimizations().
	 * @param thatClass
	 */
	private Optimizations(Class thatClass)
	{
		super();
		this.thatClass		= thatClass;
	}

	/**
	 * Obtain Optimizations object in the global scope of root Optimizations.
	 * Uses just-in-time / lazy evaluation.
	 * The first time this is called for a given ElementState class, it constructs a new Optimizations
	 * saves it in our rootOptimizationsMap, and returns it.
	 * <p/>
	 * Subsequent calls merely pass back the already created object from the rootOptimizationsMap.
	 * 
	 * @param elementState		An ElementState object that we're looking up Optimizations for.
	 * @return
	 */
	static Optimizations lookupRootOptimizations(ElementState elementState)
	{
		Class thatClass		= elementState.getClass();
//		String className	= classSimpleName(thatClass);
		String className	= thatClass.getName();
		// stay out of the synchronized block most of the time
		Optimizations result= rootOptimizationsMap.get(className);
		if (result == null)
		{
			// but still be thread safe!
			synchronized (rootOptimizationsMap)
			{
				result 		= rootOptimizationsMap.get(className);
				if (result == null)
				{
					result				= new Optimizations(thatClass);
					rootOptimizationsMap.put(className, result);
				}
			}
		}
		return result;
	}
	
	/**
	 * Obtain child Optimizations object in the local scope of this.
	 * Uses just-in-time / lazy evaluation.
	 * The first time this is called for a given ElementState class, it constructs a new Optimizations
	 * saves it in our rootOptimizationsMap, and returns it.
	 * <p/>
	 * Subsequent calls merely pass back the already created object from the rootOptimizationsMap.
	 * 
	 * @param elementState		An ElementState object that we're looking up Optimizations for.
	 * @return
	 */
	Optimizations lookupChildOptimizations(ElementState elementState)
	{
		return childOptimizationsMap.getOrCreateAndPutIfNew(elementState);
	}
	/**
	 * Get a tag translation object that corresponds to the fieldName,
	 * with this class. If necessary, form that tag translation object,
	 * and cache it.
	 * @param type TODO
	 */
	FieldToXMLOptimizations getTagMapEntry(Field field, Class<? extends ElementState> thatClass)
	{
		FieldToXMLOptimizations result= fieldToXMLOptimizationsMap.get(thatClass);
		if (result == null)
		{
			synchronized (fieldToXMLOptimizationsMap)
			{
				result		= fieldToXMLOptimizationsMap.get(thatClass);
				if (result == null)
				{
				    result = new FieldToXMLOptimizations(field, thatClass);
                    
                    fieldToXMLOptimizationsMap.put(thatClass, result);
					//debug(tagName.toString());
				}
			}
		}
		return result;
	}
	FieldToXMLOptimizations rootFieldToXMLOptimizations(Class rootClass)
	{
		FieldToXMLOptimizations result= fieldToXMLOptimizationsMap.get(rootClass);
		if (result == null)
		{
			synchronized (fieldToXMLOptimizationsMap)
			{
				result		= fieldToXMLOptimizationsMap.get(rootClass);
				if (result == null)
				{
				    result = new FieldToXMLOptimizations(rootClass);
                    
                    fieldToXMLOptimizationsMap.put(rootClass, result);
				}
			}
		}
		return result;
		
	}
	
	/**
	 * Accessor for collection elements (no field).
     * 
	 * @param collectionTagMapEntry
	 * @param actualCollectionElementClass
	 * @return
	 */
	FieldToXMLOptimizations getTagMapEntry(FieldToXMLOptimizations collectionTagMapEntry, Class<? extends ElementState> actualCollectionElementClass)
	{
		FieldToXMLOptimizations result= fieldToXMLOptimizationsMap.get(actualCollectionElementClass);
		if (result == null)
		{
			synchronized (fieldToXMLOptimizationsMap)
			{
				result		= fieldToXMLOptimizationsMap.get(actualCollectionElementClass);
				if (result == null)
				{
				    result = new FieldToXMLOptimizations(collectionTagMapEntry, actualCollectionElementClass);
                    
                    fieldToXMLOptimizationsMap.put(actualCollectionElementClass, result);
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
	FieldToXMLOptimizations fieldToXMLOptimizations(Field field)
	{
		FieldToXMLOptimizations result= fieldToXMLOptimizationsMap.get(field);
		if (result == null)
		{
			synchronized (fieldToXMLOptimizationsMap)
			{
				result		= fieldToXMLOptimizationsMap.get(field);
				if (result == null)
				{
					result	= new FieldToXMLOptimizations(field);
//					debug(tagName.toString());
					fieldToXMLOptimizationsMap.put(field, result);
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
	 * @param translationSpace TODO
	 * @param context TODO
	 * @param node TODO
	 */
	NodeToJavaOptimizations elementNodeToJavaOptimizations(TranslationSpace translationSpace, ElementState context, Node node)
	{
		String tag				= node.getNodeName();
		return elementNodeToJavaOptimizations(translationSpace, context, tag);
	}
	NodeToJavaOptimizations elementNodeToJavaOptimizations(TranslationSpace translationSpace, ElementState context, String tag)
	{
		NodeToJavaOptimizations result	= nodeToJavaOptimizationsMap.get(tag);
		
		if (result == null)
		{
			result				= new NodeToJavaOptimizations(translationSpace, this, context, tag, false);
			nodeToJavaOptimizationsMap.put(tag, result);
		}
		return result;
	}

	/**
	 * Lookup, and create if necessary, the NodeToJavaOptimizations for an attribute.
	 * 
	 * @param translationSpace
	 * @param context
	 * @param node
	 * @return
	 * 
	 * @deprecated
	 */
	NodeToJavaOptimizations attributeNodeToJavaOptimizations(TranslationSpace translationSpace, ElementState context, Node node)
	{
		String tag				= node.getNodeName();
		NodeToJavaOptimizations result	= nodeToJavaOptimizationsMap.get(tag);
		
		if (result == null)
		{
			if (tag.startsWith("xmlns:"))
			{
				String nameSpaceID	= tag.substring(6);
				if (!containsNameSpaceByNSID(nameSpaceID))
				{
					registerNameSpace(translationSpace, nameSpaceID, node.getNodeValue());
				}
			}
			result				= new NodeToJavaOptimizations(translationSpace, this, context, tag, true);
			nodeToJavaOptimizationsMap.put(tag, result);
		}
		return result;
	}

	/**
	 * Lookup, and create if necessary, the NodeToJavaOptimizations for an attribute.
	 * 
	 * @param translationSpace
	 * @param context
	 * @param node
	 * @return
	 */
	NodeToJavaOptimizations attributeNodeToJavaOptimizations(TranslationSpace translationSpace, ElementState context, String tag, String value)
	{
		NodeToJavaOptimizations result	= nodeToJavaOptimizationsMap.get(tag);
		
		if (result == null)
		{
			if (tag.startsWith("xmlns:"))
			{
				String nameSpaceID	= tag.substring(6);
				if (!containsNameSpaceByNSID(nameSpaceID))
				{
					registerNameSpace(translationSpace, nameSpaceID, value);
				}
			}
			result				= new NodeToJavaOptimizations(translationSpace, this, context, tag, true);
			nodeToJavaOptimizationsMap.put(tag, result);
		}
		return result;
	}
	final Object AFO_LOCK	= new Object();
	
	int quickNumElements()
	{
		return elementFields == null ? 0 : elementFields.size();
	}
	
	ArrayList<FieldToXMLOptimizations>	attributeFieldOptimizations()
	{
		ArrayList<FieldToXMLOptimizations>	result	= attributeFieldOptimizations;
		if (result == null)
		{
			synchronized (AFO_LOCK)
			{
				result	= attributeFieldOptimizations;
				if (result == null)
				{
					ArrayList<Field> attributeFields2	= attributeFields();
					int numAttributes					= attributeFields2.size();
					if (numAttributes == 0)
						result				= new ArrayList<FieldToXMLOptimizations>(1);
					else
					{
						result				= new ArrayList<FieldToXMLOptimizations>(numAttributes);
						for (int i=0; i<numAttributes; i++)
						{
							result.add(this.fieldToXMLOptimizations(attributeFields2.get(i)));
						}
					}
				}
				this.attributeFieldOptimizations		= result;
			}
		}
		return result;
	}
	
	final Object EFO_LOCK	= new Object();
	
	ArrayList<FieldToXMLOptimizations>	elementFieldOptimizations()
	{
		ArrayList<FieldToXMLOptimizations>	result	= elementFieldOptimizations;
		if (result == null)
		{
			synchronized (EFO_LOCK)
			{
				result	= elementFieldOptimizations;
				if (result == null)
				{
					ArrayList<Field> elementFields2	= elementFields();
					int numElements					= elementFields2.size();
					if (numElements == 0)
						result				= new ArrayList<FieldToXMLOptimizations>(1);
					else
					{
						result				= new ArrayList<FieldToXMLOptimizations>(numElements);
						for (int i=0; i<numElements; i++)
						{
							result.add(this.fieldToXMLOptimizations(elementFields2.get(i)));
						}
					}
				}
				this.elementFieldOptimizations		= result;
			}
		}
		return result;
	}
	/**
	 * Get the fields that are represented as attributes for the class we're optimizing.
	 * Uses lazy evaluation -- while derive the answer for attributefieldbs and elementFields, and cache it.
	 * 
	 * @return	ArrayList of Field objects.
	 */
	ArrayList<Field> attributeFields()
	{
		ArrayList<Field> result	= attributeFields;
		if (result == null)
		{
			synchronized (this)
			{
				result		= attributeFields;	// check again inside the synchronized block
				if (result == null)
				{
					getAndOrganizeFields();
				}
			}
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
	public ArrayList<Field> elementFields()
	{
		ArrayList<Field> result	= elementFields;
		if (result == null)
		{
			synchronized (this)
			{
				result		= elementFields;	// check again inside the synchronized block
				if (result == null)
				{
					getAndOrganizeFields();
				}
			}
			result			= elementFields;
		}
		return result;
	}
	
	/**
	 * Get the map of fields that we translate for this class.
	 * Uses lazy evaluation; will call getAndOrganizeFields() to build it
	 * (and also the attributeFields and elementFields ArrayLists) if this is the 1st
	 * call for the class.
	 * 
	 * @return
	 */
	private HashMap<String, Field> fieldsMap()
	{
		HashMap<String, Field> result	= fieldsMap;
		if (result == null)
		{
			synchronized (this)
			{
				result		= fieldsMap;	// check again inside the synchronized block
				if (result == null)
				{
					getAndOrganizeFields();
				}
			}
			result			= fieldsMap;
		}
		return result;
	}
	
	/**
	 * Performs the lazy evaluation to get attribute and element field collections.
	 * Dispatches to the correct routine for this, based on ElementState.declarationStyle().
	 *
	 */
	private synchronized void getAndOrganizeFields()
	{
		attributeFields		= new ArrayList<Field>();
		elementFields		= new ArrayList<Field>();
		fieldsMap			= new HashMap<String, Field>();
		ElementState.DeclarationStyle ds = ElementState.declarationStyle();
		switch (ds)
		{
		case PUBLIC:
			getAndOrganizeFieldsPublic(thatClass, attributeFields, elementFields);
			break;
		case ANNOTATION:
		case TRANSIENT:
			getAndOrganizeFieldsRecursive(thatClass, attributeFields, elementFields,
										  (ds == ElementState.DeclarationStyle.TRANSIENT));
			break;
		default:
			throw new RuntimeException("Unsupported declaration style: " + ds);
		}
	}
	/**
	 * Performs the lazy evaluation to get attribute and element field collections.
	 * Uses the hip new annotation stylee.
	 * Uses the old PUBLIC declaration stylee.
	 * <p/>
	 * Must be called from getAndOrganizeFields() !
	 */
	private void getAndOrganizeFieldsPublic(Class thatClass,
			ArrayList<Field> attributeFields, ArrayList<Field> elementFields)
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
			mapField(thatField);
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
	private void getAndOrganizeFieldsRecursive(Class thatClass,
			ArrayList<Field> attributeFields, ArrayList<Field> elementFields, 
			boolean transientDeclarationStyle)
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
			//mapField(thatField);
			if (XmlTools.representAsAttribute(thatField))
			{
				mapField(thatField);
				attributeFields.add(thatField);
				thatField.setAccessible(true);
			}
			else if (XmlTools.representAsLeafOrNested(thatField))
			{
				mapField(thatField);
				elementFields.add(thatField);
				thatField.setAccessible(true);
				
				// look for declared tag for @xml_collection
				ElementState.xml_collection collectionAnnotation		
					= thatField.getAnnotation(ElementState.xml_collection.class);
				if (collectionAnnotation != null)
				{
					processCollectionDeclaredWithTag(thatField, collectionAnnotation);
				}
				else
				{
					// look for declared tag for @xml_map
					ElementState.xml_map mapAnnotation
						= thatField.getAnnotation(ElementState.xml_map.class);
					if (mapAnnotation != null)
					{
						processMapDeclaredWithTag(thatField, mapAnnotation);
					}				
				}
			}
			// else -- ignore non-annotated fields
		}
		if (thatClass.isAnnotationPresent(xml_inherit.class) || transientDeclarationStyle) 
		{	// recurse on super class
			Class superClass	= thatClass.getSuperclass();
			if (superClass != null)
				getAndOrganizeFieldsRecursive(superClass, attributeFields, elementFields, transientDeclarationStyle);
		}
	}

	/**
	 * @param thatField
	 * @param tagFromAnnotation
	 */
	private void processCollectionDeclaredWithTag(Field thatField, ElementState.xml_collection collectionAnnotation)
	{
		String tagFromAnnotation	= collectionAnnotation.value();
		Class fieldClass			= thatField.getType();
		if (Collection.class.isAssignableFrom(fieldClass))
		{
			if ((tagFromAnnotation != null) && !"".equals(tagFromAnnotation))
			{ 
				//note! we *really* want to wait until here before we allocate storage for this HashMap!
				collectionFieldsByTag().put(tagFromAnnotation, thatField);
			}
		} 
		else
			annotatedFieldError(thatField, tagFromAnnotation, "collection");
	}

	/**
	 * @param thatField
	 * @param mapAnnotation
	 */
	private void processMapDeclaredWithTag(Field thatField, ElementState.xml_map mapAnnotation)
	{
		String tagFromAnnotation	= mapAnnotation.value();
		Class fieldClass			= thatField.getType();
		if (Map.class.isAssignableFrom(fieldClass))
		{
			if ((tagFromAnnotation != null) && !"".equals(tagFromAnnotation))
			{ 
				//note! we *really* want to wait until here before we allocate storage for this HashMap!
				mapFieldsByTag().put(tagFromAnnotation, thatField);
			}
		} 
		else
			annotatedFieldError(thatField, tagFromAnnotation, "map");
	}

	HashMap<String, Field> collectionFieldsByTag()
	{
		HashMap<String, Field> result	= this.collectionFieldsByTag;
		if (result == null)
		{
			result	= new HashMap<String, Field>();
			this.collectionFieldsByTag	= result;
		}
		return result;
	}
	HashMap<String, Field> mapFieldsByTag()
	{
		HashMap<String, Field> result	= this.mapFieldsByTag;
		if (result == null)
		{
			result	= new HashMap<String, Field>();
			this.mapFieldsByTag			= result;
		}
		return result;
	}
	/**
	 * @param thatField
	 * @param tagFromAnnotation
	 * @param required
	 */
	private void annotatedFieldError(Field thatField, String tagFromAnnotation, String required)
	{
		String tagMsg = ((tagFromAnnotation == null) || (tagFromAnnotation.length() == 0)) ? "" 
				: ("\"" + tagFromAnnotation + "\"");

		error("@xml_collection(" + tagMsg + ") declared as type " + 
				thatField.getType().getSimpleName() +" for field named "+ thatField.getName() + ", which is not a " + required+ ".");
	}
	
	
	/**
	 * Add an entry to our map of Field objects, using the field's name as the key.
	 * Can only be called by routines that guaranty that the fieldsMap has already been
	 * created and populated with entries.
	 * 
	 * @param fieldsMap
	 * @param thatField
	 */
	private void mapField(Field thatField)
	{
		fieldsMap.put(thatField.getName(), thatField);
	}
/*
	private void mapMap(Field thatField)
	{
		mapsMap.put(thatField.getName(), thatField);
	}

	private void mapCollection(Field thatField)
	{
		collectionsMap.put(thatField.getName(), thatField);
	}
*/
	/**
	 * Get the field asssociated with fieldName.
	 * If necessary (that is, the first time), this routine may make calls to do all the work
	 * for creating the fieldsMap and fields ArrayLists.
	 */
	Field getField(String fieldName)
	{
		return fieldsMap().get(fieldName);
	}
	
	/**
	 * Lookup a collection object by its tag name.
	 * This seeks a field declared with @xml_collection(tag).
	 * 
	 * @param tag
	 * @return	The Field object for the correctly parameterized Collection.
	 */
	Field getCollectionFieldByTag(String tag)
	{
		return (collectionFieldsByTag == null) ? null : collectionFieldsByTag.get(tag);
	}
	/**
	 * Lookup a collection object by its tag name.
	 * This seeks a field declared with @xml_collection(tag).
	 * 
	 * @param tag
	 * @return	The Field object for the correctly parameterized Collection.
	 */
	Field getMapFieldByTag(String tag)
	{
		return (mapFieldsByTag == null) ? null : mapFieldsByTag.get(tag);
	}

	public String toString()
	{
		return "Optimizations[" + thatClass.getName() + "]"; 
	}
	
	/**
	 * Add an entry to the global table of class to URNs.
	 * 
	 * @param urn
	 * @param nameSpaceElementState
	 */
	public void registerNameSpaceByURN(String urn, Class<? extends ElementState> nameSpaceElementState)
	{
		nameSpaceRegistryByURN.put(urn, nameSpaceElementState);
	}
	void registerNameSpace(TranslationSpace translationSpace, String nsID, String urn)
	{
		//TODO first look in the TranslationSpace for a mapping
		Class<? extends ElementState> nameSpaceClass	= translationSpace.lookupNameSpaceByURN(urn);
		
		if (nameSpaceClass == null)
		{	// now check the global rootOptimizationsMap
			nameSpaceClass								= nameSpaceRegistryByURN.get(urn);
			if (nameSpaceClass == null)
				return;
		}
		
		nameSpacesByID.put(nsID, nameSpaceClass);
		//TODO add entries to other useful data structures
	}
	boolean containsNameSpaceByNSID(String nsID)
	{
		return nameSpacesByID.containsKey(nsID);
	}
	Class<? extends ElementState> lookupNameSpaceByNSID(String nsID)
	{
		return nameSpacesByID.get(nsID);
	}

	/* static */ class OptimizationsMap extends HashMapWriteSynch3<String, Class, Optimizations>
	implements ValueFactory<Class, Optimizations>
	{
		public Optimizations getOrCreateAndPutIfNew(ElementState elementState)
		{
			return super.getOrCreateAndPutIfNew(elementState.getClass());
		}
		
		@Override
		protected String createKey(Class intermediate)
		{
			return intermediate.getName();
		}
		
		public Optimizations createValue(Class key)
		{
			return new Optimizations(key);
		}
	}
}
