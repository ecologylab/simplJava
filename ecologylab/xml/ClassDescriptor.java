package ecologylab.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import ecologylab.generic.Debug;
import ecologylab.generic.HashMapArrayList;
import ecologylab.generic.HashMapWriteSynch3;
import ecologylab.generic.ReflectionTools;
import ecologylab.generic.ValueFactory;
import ecologylab.xml.TranslationScope.TranslationEntry;
import ecologylab.xml.types.scalar.ScalarType;

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
public class ClassDescriptor extends Debug
implements ClassTypes
{
	/**
	 * Class object that we are holding optimizations for.
	 */
	final Class<? extends ElementState>					thatClass;
	
	ClassDescriptor										parent;
	
	private String										nameSpaceID;
	
	/**
	 * A map of root level Optimizations objects.
	 * The keys are simple class names.
	 */
	//TODO -- replace with OptimizationsMap
	private static final HashMap<String, ClassDescriptor>	rootOptimizationsMap	= new HashMap<String, ClassDescriptor>();
//	private static final OptimizationsMap	rootOptimizationsMap	= new OptimizationsMap();
	
	/**
	 * Map of Optimizations objects for (the classes of) our children.
	 * We need to collect these in this scope, because there could be different metalanguage declarations.
	 * (IS THIS TRUE? EVEN I AM NOT SURE. -- ANDRUID 11/2/07)
	 */
	private final OptimizationsMap						childOptimizationsMap	= new OptimizationsMap();
		
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
	private HashMap<String, ElementDescriptor>	nodeToJavaOptimizationsMap	= new HashMap<String, ElementDescriptor>();
	
	/**
	 * The fields that are represented as attributes for the class we're optimizing.
	 */
	private ArrayList<Field>					attributeFields;
	
	private ArrayList<FieldToXMLOptimizations>	attributeFieldOptimizations;
	
	/**
	 * These are pseudo-FieldOptimizations, used to generate xmnls: 
	 * "attributes".
	 */
	private ArrayList<FieldToXMLOptimizations>	xmlnsAttributeOptimizations;
	
	/**
	 * The fields that are represented as nested elements (including leaf nodes)
	 * for the class we're optimizing.
	 */
	private ArrayList<Field>					elementFields;
	
	private ArrayList<FieldToXMLOptimizations>	elementFieldOptimizations;
	
	private Field								scalarTextField;
	
	/**
	 * Handles a text node.
	 */
	private ElementDescriptor				scalarTextN2jo;
	
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
	
	private HashMap<String, Class<? extends ElementState>>	nameSpaceClassesById	= new HashMap<String, Class<? extends ElementState>>();
	
	
	/**
	 * Constructor is private, because values of this type are accessed through lazy evaluation, and cached.
	 * See also lookupRoot(), lookupChildOptimizations().
	 * @param thatClass
	 */
	private ClassDescriptor(Class<? extends ElementState> thatClass)
	{
		this(thatClass, null);
	}

	/**
	 * Constructor is private, because values of this type are accessed through lazy evaluation, and cached.
	 * See also lookupRoot(), lookupChildOptimizations().
	 * 
	 * @param 	thatClass
	 * @param	parent		Parent optimizations.
	 */
	private ClassDescriptor(Class<? extends ElementState> thatClass, ClassDescriptor parent)
	{
		super();
		this.thatClass		= thatClass;
		setParent(parent);
		
		getAndOrganizeFields();
	}

	void setParent(ClassDescriptor parent)
	{
		this.parent			= parent;
		//FIXME -- instead of inheriting fully, implement static lexical scoping
		if (parent != null)
			this.nameSpaceClassesById		= parent.nameSpaceClassesById;
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
	static ClassDescriptor lookupRootOptimizations(ElementState elementState)
	{
		Class<? extends ElementState> thatClass		= elementState.getClass();

		return lookupRootOptimizations(thatClass);
	}

	/**
	 * Obtain Optimizations object in the global scope of root Optimizations.
	 * Uses just-in-time / lazy evaluation.
	 * The first time this is called for a given ElementState class, it constructs a new Optimizations
	 * saves it in our rootOptimizationsMap, and returns it.
	 * <p/>
	 * Subsequent calls merely pass back the already created object from the rootOptimizationsMap.
	 * 
	 * @param thatClass
	 * @return
	 */
	static ClassDescriptor lookupRootOptimizations(Class<? extends ElementState> thatClass)
	{
		String className	= thatClass.getName();
		// stay out of the synchronized block most of the time
		ClassDescriptor result= rootOptimizationsMap.get(className);
		if (result == null)
		{
			// but still be thread safe!
			synchronized (rootOptimizationsMap)
			{
				result 		= rootOptimizationsMap.get(className);
				if (result == null)
				{
					result				= new ClassDescriptor(thatClass);
					rootOptimizationsMap.put(className, result);
				}
			}
		}
		return result;
	}
	
	void setNameSpaceID(String nameSpaceID)
	{
		this.nameSpaceID	= nameSpaceID + ":";
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
	//TODO -- do we need to pass NamespaceID through here!?
	ClassDescriptor lookupChildOptimizations(ElementState elementState)
	{
		return childOptimizationsMap.getOrCreateAndPutIfNew(elementState);
	}
	
	ClassDescriptor lookupChildOptimizations(Class<ElementState> thatClass)
	{
		return childOptimizationsMap.getOrCreateAndPutIfNew(thatClass);
	}


	/**
	 * Get a tag translation object that corresponds to the fieldName,
	 * with this class. If necessary, form that tag translation object,
	 * and cache it.
	 * @param type TODO
	 */
	FieldToXMLOptimizations fieldToXMLOptimizations(Field field, Class<? extends ElementState> thatClass)
	{
		FieldToXMLOptimizations result= fieldToXMLOptimizationsMap.get(thatClass);
		if (result == null)
		{
			synchronized (fieldToXMLOptimizationsMap)
			{
				result		= fieldToXMLOptimizationsMap.get(thatClass);
				if (result == null)
				{
				    result = new FieldToXMLOptimizations(this, field, thatClass);
                    
                    fieldToXMLOptimizationsMap.put(thatClass, result);
					//debug(tagName.toString());
				}
			}
		}
		return result;
	}
	
	/**
	 * Form a pseudo-FieldToXMLOptimizations-object for a root element.
	 * We say pseudo, because there is no Field corresponding to this element.
	 * The pseudo-FieldToXMLOptimizations-object still guides the translation process.
	 * 
	 * @param rootClass
	 * @return
	 */
	public FieldToXMLOptimizations rootFieldToXMLOptimizations(Class rootClass)
	{
		FieldToXMLOptimizations result= fieldToXMLOptimizationsMap.get(rootClass);
		if (result == null)
		{
			synchronized (fieldToXMLOptimizationsMap)
			{
				result		= fieldToXMLOptimizationsMap.get(rootClass);
				if (result == null)
				{
				    result = new FieldToXMLOptimizations(this, rootClass, this.nameSpaceID);
                    
                    fieldToXMLOptimizationsMap.put(rootClass, result);
				}
			}
		}
		return result;
		
	}
	
	/**
	 * Descriptor for collection elements (no field).
     * 
	 * @param collectionTagMapEntry
	 * @param actualCollectionElementClass
	 * @return
	 */
	FieldToXMLOptimizations fieldToJavaOptimizations(FieldToXMLOptimizations collectionTagMapEntry, Class<? extends ElementState> actualCollectionElementClass)
	{
		FieldToXMLOptimizations result= fieldToXMLOptimizationsMap.get(actualCollectionElementClass);
		if (result == null)
		{
			synchronized (fieldToXMLOptimizationsMap)
			{
				result		= fieldToXMLOptimizationsMap.get(actualCollectionElementClass);
				if (result == null)
				{
				    result = new FieldToXMLOptimizations(this, collectionTagMapEntry, actualCollectionElementClass);
                    
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
	 * @param nameSpacePrefix TODO
	 */
	FieldToXMLOptimizations fieldToXMLOptimizations(Field field, String nameSpacePrefix)
	{
		FieldToXMLOptimizations result= fieldToXMLOptimizationsMap.get(field);
		if (result == null)
		{
			synchronized (fieldToXMLOptimizationsMap)
			{
				result		= fieldToXMLOptimizationsMap.get(field);
				if (result == null)
				{
					result	= new FieldToXMLOptimizations(this, field, nameSpacePrefix);
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
	ElementDescriptor elementNodeToJavaOptimizations(TranslationScope translationSpace, ElementState context, Node node)
	{
		String tag				= node.getNodeName();
		return nodeToJavaOptimizations(translationSpace, context, tag, false);
	}
	
	final Object N2JO_TAG_FIELDS_LOCK		= new Object();
	
	/**
	 * Lookup, and create if necessary, the NodeToJavaOptimizations for an attribute.
	 * 
	 * @param translationSpace
	 * @param context
	 * @param isAttribute 	true for attributes, false for elements
	 * @param node
	 * @return
	 */
	ElementDescriptor nodeToJavaOptimizations(TranslationScope translationSpace, ElementState context, String tag, boolean isAttribute)
	{
		ElementDescriptor result	= nodeToJavaOptimizationsMap.get(tag);
		
		if (result == null)
		{
			synchronized (N2JO_TAG_FIELDS_LOCK)
			{
				result	= nodeToJavaOptimizationsMap.get(tag);
				
				if (setupTranslateFromXML(translationSpace, context))
					result	= nodeToJavaOptimizationsMap.get(tag);	// try again after setup
					
				if (result == null)
				{
					result	= new ElementDescriptor(translationSpace, this, context, tag, isAttribute);
					nodeToJavaOptimizationsMap.put(tag, result);
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @return		true if setup was performed. false if it was performed previously.
	 */
	boolean setupTranslateFromXML(TranslationScope translationSpace, ElementState context)
	{
		boolean result		= !xmlTagFieldsAreIndexed;
		if (result)
		{
			xmlTagFieldsAreIndexed	= true;
			indexSpecialMappingsForFields(this.attributeFields(), true, translationSpace, context);
			indexSpecialMappingsForFields(this.elementFields(), false, translationSpace, context);
			
			if (this.scalarTextN2jo != null)
			{
				scalarTextN2jo.setTranslationScope(translationSpace);
			}
		}
		return result;
	}
	final Object AFO_LOCK	= new Object();
	
	int quickNumElements()
	{
		return elementFields == null ? 0 : elementFields.size();
	}
	
	public ArrayList<FieldToXMLOptimizations>	attributeFieldOptimizations()
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
							result.add(this.fieldToXMLOptimizations(attributeFields2.get(i), (String) null));
						}
					}
				}
				this.attributeFieldOptimizations		= result;
			}
		}
		return result;
	}
	
	final Object EFO_LOCK	= new Object();
	
	public ArrayList<FieldToXMLOptimizations>	elementFieldOptimizations()
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
							result.add(this.fieldToXMLOptimizations(elementFields2.get(i), this.nameSpaceID));
						}
					}
				}
				this.elementFieldOptimizations		= result;
			}
		}
		return result;
	}
	
	/**
	 * Build and return an ArrayList with Field objects for all the annotated fields in this class.
	 * 
	 * @return	ArrayList of Field objects. Could be empty. Never null.
	 */
	public ArrayList<Field> getFields()
	{
		ArrayList<Field> attributeFields	= attributeFields();
		ArrayList<Field> elementFields		= elementFields();
		ArrayList<Field> result				= new ArrayList<Field>(attributeFields.size() + elementFields.size());
		
		for (Field attrField : attributeFields)
			result.add(attrField);
		
		for (Field elementField : elementFields)
			result.add(elementField);
		
		return result;
	}
	
	private HashMapArrayList<String, FieldDescriptor>	fieldDescriptors;
	
	/**
	 * Build and return an ArrayList with Field objects for all the annotated fields in this class.
	 * Use lazy evaluation to cache this value.
	 * 
	 * @return	HashMapArrayList of Field objects, using the XML tag name for each field
	 * (not its Java field name!) as the keys. Could be empty. Never null.
	 */
	HashMapArrayList<String, FieldDescriptor> getFieldDescriptorsForThis(Class<? extends FieldDescriptor> fieldDescriptorClass)
	{
		HashMapArrayList<String, FieldDescriptor> result	= fieldDescriptors;
		if (result == null)
		{
			result				= createFieldDescriptors(fieldDescriptorClass);
			this.fieldDescriptors	= result;
		}
		return result;
	}
	
	/**
	 * Construct a set of FieldDescriptor objects for the class.
	 * 
	 * @param thatClass
	 * @return
	 */
	public static HashMapArrayList<String, FieldDescriptor> getFieldDescriptors(Class<? extends ElementState> thatClass)
	{
		return getFieldDescriptors(thatClass, null);
	}

	/**
	 * Construct a set of FieldDescriptor objects for the class.
	 * 
	 * @param <T>
	 * @param thatClass
	 * @param fieldDescriptorClass		Subclass of FieldDescriptor to use to construct.
	 * @return
	 */
	public static<T extends FieldDescriptor> HashMapArrayList<String, FieldDescriptor> 
	getFieldDescriptors(Class<? extends ElementState> thatClass, Class<T> fieldDescriptorClass)
	{
		ClassDescriptor thatClassOptimizations	= lookupRootOptimizations(thatClass);
		
		HashMapArrayList<String, FieldDescriptor> result	= null;
		
		if (thatClass != null)
		{
			result								= thatClassOptimizations.getFieldDescriptorsForThis(fieldDescriptorClass);
		}
		return result;
	}

	static final Class[] NEW_FIELD_DESCRIPTOR_TYPES =
	{
		FieldToXMLOptimizations.class,
	};
	/**
	 * Build and return an ArrayList with Field objects for all the annotated fields in this class.
	 * 
	 * @param fieldDescriptorClass	The Class to use for instantiating each FieldDescriptor.
	 * 								The default is FieldDescriptor, but class objects may be passed in that extend that class.
	 * 
	 * @return	HashMapArrayList of Field objects, using the XML tag name for each field
	 * (not its Java field name!) as the keys. Could be empty. Never null.
	 */
	private HashMapArrayList<String, FieldDescriptor> createFieldDescriptors(Class<? extends FieldDescriptor> fieldDescriptorClass)
	{
		ArrayList<FieldToXMLOptimizations> attributeF2XOs	= attributeFieldOptimizations();
		ArrayList<FieldToXMLOptimizations> elementF2XOs		= elementFieldOptimizations();
		
		HashMapArrayList<String, FieldDescriptor> result		= new HashMapArrayList<String, FieldDescriptor>(attributeF2XOs.size() + elementF2XOs.size());

		for (FieldToXMLOptimizations attrF2XO		: attributeF2XOs)
		{
			FieldDescriptor	fDescriptor	= (fieldDescriptorClass == null) ? 
					new FieldDescriptor(attrF2XO) : createFieldDescriptor(fieldDescriptorClass, attrF2XO);
					
			String tagName 				= attrF2XO.tagName();
			if (!result.containsKey(tagName))
				result.put(tagName, fDescriptor);
		}
		
		for (FieldToXMLOptimizations elementF2XO	: elementF2XOs)
		{
			FieldDescriptor	fDescriptor	= (fieldDescriptorClass == null) ? 
					new FieldDescriptor(elementF2XO) : createFieldDescriptor(fieldDescriptorClass, elementF2XO);
					
			String tagName 				= elementF2XO.tagName();
			if (!result.containsKey(tagName))
				result.put(tagName, fDescriptor);
		}
		return result;
	}
	
	private FieldDescriptor createFieldDescriptor(Class<? extends FieldDescriptor> fieldDescriptorClass, FieldToXMLOptimizations attrF2XO)
	{
		Object[] args	= new Object[1];
		args[0]			= attrF2XO;
		
		return ReflectionTools.getInstance(fieldDescriptorClass, NEW_FIELD_DESCRIPTOR_TYPES, args);
	}
	/**
	 * Get the fields that are represented as attributes for the class we're optimizing.
	 * 
	 * @return	ArrayList of Field objects.
	 */
	ArrayList<Field> attributeFields()
	{
		return attributeFields;
	}
	/**
	 * Get the fields that are represented as nested elements (including leaf nodes)
	 * for the class we're optimizing.
	 * 
	 * @return	ArrayList of Field objects.
	 */
	public ArrayList<Field> elementFields()
	{
		return elementFields;
	}
	
	/**
	 * Performs the lazy evaluation to get attribute and element field collections.
	 * Dispatches to the correct routine for this, based on ElementState.declarationStyle().
	 *
	 */
	private synchronized void getAndOrganizeFields()
	{
		attributeFields	= new ArrayList<Field>();
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
			if (XMLTools.isScalarValue(thatField) && !XMLTools.representAsLeafNode(thatField))
			{
				attributeFields.add(thatField);
			}
			else
			{
				elementFields.add(thatField);
			}
		}
	}
	
	ArrayList<Field>	annotatedFields;
	
	ArrayList<Field>	annotatedFields()
	{
		ArrayList<Field>	result	= annotatedFields;
		if (result == null)
		{
			Field[] fields		= thatClass.getDeclaredFields();
			
			result						= new ArrayList<Field>(fields.length);
			for (Field thatField : fields)
			{
				if (XMLTools.representAsLeafOrNested(thatField) || 
					 XMLTools.representAsAttribute(thatField) ||
					 thatField.isAnnotationPresent(ElementState.xml_text.class) ||
					 thatField.isAnnotationPresent(ElementState.xml_collection.class) ||
					 thatField.isAnnotationPresent(ElementState.xml_map.class))
				{
					thatField.setAccessible(true);
					result.add(thatField);
				}
			}
			annotatedFields = result;
		}
		return result;
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
		//TODO -- use annotated fields instead!
		Field[] fields		= thatClass.getDeclaredFields();
		
		for (int i = 0; i < fields.length; i++)
		{
			Field thatField			= fields[i];
			
			// skip static fields, since we're saving instances,
			// and inclusion w each instance would be redundant.
			if ((thatField.getModifiers() & Modifier.STATIC) == Modifier.STATIC)
			{
//				debug("Skipping " + thatField + " because its static!");
				continue;
			}
			//mapField(thatField);
			if (XMLTools.representAsAttribute(thatField))
			{
				indexField(attributeFields, thatField);
//				String tag	= 
			}
			else if (XMLTools.representAsLeafOrNested(thatField))
			{
				indexField(elementFields, thatField);
				
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
			else if (thatField.isAnnotationPresent(ElementState.xml_text.class))
			{
				// Special field for a typed text node value
				scalarTextField		= thatField;
				scalarTextN2jo 		= new ElementDescriptor(this, thatField);
				
				thatField.setAccessible(true);
            
				//TODO -- is this line necessary? desirable?
				mapField(thatField);
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
	
	boolean xmlTagFieldsAreIndexed;
	
	/**
	 * For a set of fields, establish special tag-object mappings for use in translating from XML.
	 * <p/>
	 * Effects fields declared with <code>@xml_tag</code>, <code>@xml_class</code>, or <code>@xml_classes</code>.
	 * 
	 * @param themFields
	 * @param isAttribute
	 * @param tspace
	 * @param context
	 */
	private void indexSpecialMappingsForFields(ArrayList<Field> themFields, boolean isAttribute, TranslationScope tspace, ElementState context)
	{
		for (Field thatField : themFields)
		{
			ElementDescriptor n2jo		= null;
			ElementState.xml_tag tagAnnotation 	= thatField.getAnnotation(ElementState.xml_tag.class);
			boolean isNested 					= thatField.isAnnotationPresent(ElementState.xml_nested.class);
			if (tagAnnotation != null)
			{
				n2jo	= registerTagOptimizationsIfNeeded(isAttribute, tspace, thatField, tagAnnotation);
			}
			else
			{
				if (isNested)
				{
					ElementState.xml_classes classesAnnotation 	= thatField.getAnnotation(ElementState.xml_classes.class);
					if (classesAnnotation != null)
					{
						Class<? extends ElementState> thoseClasses[]	= classesAnnotation.value();
						int length				= thoseClasses.length;
						for (int i=0; i<length; i++)
						{
							Class<? extends ElementState> thisClass		= thoseClasses[i];
							registerN2JOByClass(tspace, thatField, thisClass);
						}
					}
					else
					{
						ElementState.xml_class classAnnotation 	= thatField.getAnnotation(ElementState.xml_class.class);
						if (classAnnotation != null)
						{
							n2jo				= registerN2JOByClass(tspace, thatField, classAnnotation.value());
						}
						else
						{
							ElementState.xml_scope scopeAnnotation 	= thatField.getAnnotation(ElementState.xml_scope.class);
							if (scopeAnnotation != null)
							{
								TranslationScope translationScope				= TranslationScope.lookup(scopeAnnotation.value());
								if (translationScope != null)
								{
									Collection<TranslationEntry> scopeEntries	= translationScope.getEntries();
									for (TranslationEntry entry : scopeEntries)
									{
										registerN2JOByClass(tspace, thatField, entry.thisClass);										
									}
								}
							}
						}
					}
				}
			}
			if (isNested || (n2jo != null))
			{
				ElementState.xml_other_tags otherTagsAnnotation 	= thatField.getAnnotation(ElementState.xml_other_tags.class);
				if (otherTagsAnnotation != null)
				{
					registerOtherTagsOptimizationsIfNeeded(n2jo, isAttribute, tspace, thatField, otherTagsAnnotation);
				}
			}
		}
	}

	/**
	 * Identify or create a NodeToJavaOptimizations object.
	 * Create a mapping between that and a tag derived from its class name.
	 * 
	 * @param tspace
	 * @param thatField
	 * @param thatClass
	 */
	private ElementDescriptor registerN2JOByClass(TranslationScope tspace, Field thatField,
			Class thatClass)
	{
		ElementDescriptor n2jo	= 
			new ElementDescriptor(tspace, this, thatField, thatClass);
		nodeToJavaOptimizationsMap.put(n2jo.tag(), n2jo);
		return n2jo;
	}

	/**
	 * Identify or create a NodeToJavaOptimizations object.
	 * Create a mapping between that and the value of a tag annotation.
	 * 
	 * @param isAttribute
	 * @param tspace
	 * @param thatField
	 * @param tagAnnotation
	 * 
	 * @return	NodeToJavaOptimizations that was registered, or null.
	 */
	private ElementDescriptor registerTagOptimizationsIfNeeded(boolean isAttribute, TranslationScope tspace, Field thatField, ElementState.xml_tag tagAnnotation)
	{
		String thatTag					= tagAnnotation.value();
		ElementDescriptor	result	= null;
		if ((thatTag != null) && (thatTag.length() > 0))
		{
			result						= nodeToJavaOptimizationsMap.get(thatTag);
			if (result == null)
			{
				result					= new ElementDescriptor(tspace, this, thatField, thatTag, isAttribute);
				nodeToJavaOptimizationsMap.put(thatTag, result);
			}
		}
		return result;
	}
	
	private boolean registerOtherTagsOptimizationsIfNeeded(ElementDescriptor fieldN2jo, boolean isAttribute, TranslationScope tspace, Field thatField, ElementState.xml_other_tags otherTagsAnnotation)
	{
		String[] otherTags		= XMLTools.otherTags(otherTagsAnnotation);
		final boolean result	= (otherTags != null);
		if (result)
		{
			for (String otherTag : otherTags)
			{
				
				ElementDescriptor n2jo	= nodeToJavaOptimizationsMap.get(otherTag);
				if (n2jo == null)
				{
					if (fieldN2jo != null)
						n2jo					= fieldN2jo;
					else
						n2jo					= new ElementDescriptor(tspace, this, thatField, otherTag, isAttribute);
					nodeToJavaOptimizationsMap.put(otherTag, n2jo);
				}
			}
		}
		return result;
	}
	
	/**
	 * Add this Field to various data structures, and make it accessible for reflection.
	 * 
	 * @param fieldsArrayList
	 * @param thatField
	 */
	//TODO -- get rid of either fieldsArrayList or isAttribute
	private void indexField(ArrayList<Field> fieldsArrayList, Field thatField)
	{
		mapField(thatField);
		fieldsArrayList.add(thatField);
		thatField.setAccessible(true);
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
			HashMap<String, Field> collectionFieldsByTag = collectionFieldsByTag();
			if ((tagFromAnnotation != null) && !"".equals(tagFromAnnotation))
			{ 
				//note! we *really* want to wait until here before we allocate storage for this HashMap!
				collectionFieldsByTag.put(tagFromAnnotation, thatField);
				
				ElementState.xml_other_tags otherTagsAnnotation 	= thatField.getAnnotation(ElementState.xml_other_tags.class);
				if (otherTagsAnnotation != null)
				{
					String[] otherTags	= XMLTools.otherTags(otherTagsAnnotation);
					for (String otherTag : otherTags)
					{
						if ((otherTag != null) && (otherTag.length() > 0))
						{
							collectionFieldsByTag.put(otherTag, thatField);
						}
					}
				}
			}
			else
			{
				// no direct annotation of tag for children. how about classes?
				ElementState.xml_classes classesAnnotation	= thatField.getAnnotation(ElementState.xml_classes.class);
				if (classesAnnotation != null)
				{
					Class[]	classes	= classesAnnotation.value();
					if (classes != null)
					{
						for (int i=0; i<classes.length; i++)
						{
							String	tagFromClass	= XMLTools.getXmlTagName(classes[i], "State");
							collectionFieldsByTag.put(tagFromClass, thatField);
						}
					}
				}
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
		return fieldsMap.get(fieldName);
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
	 * Map an XML namespace id to the class that should be instantiated to handle it.
	 * 
	 * @param translationSpace Used for error messages.
	 * @param nsID
	 * @param urn TODO
	 */
	void mapNamespaceIdToClass(TranslationScope translationSpace, String nsID, String urn)
	{
		if (!nameSpaceClassesById.containsKey(nsID))
		{
			Class<? extends ElementState> nsClass	= translationSpace.lookupNameSpaceByURN(urn);
			final boolean nsUnsupported				= (nsClass == null);
			nameSpaceClassesById().put(nsID, nsClass);
			FieldToXMLOptimizations xmlnsF2XO = new FieldToXMLOptimizations(nsID, urn, nsUnsupported);
			if (nsUnsupported)
				warning("No Namespace found in " + translationSpace + " for\t\txmlns:" + nsID +"=" + urn);
			else
			{
				debug("COOL! " + translationSpace + " \t" + nsClass.getName() + " ->\t\txmlns:" + nsID +"=" + urn);
				xmlnsAttributeOptimizations().add(xmlnsF2XO);
			}
		}
	}
	/**
	 * Lazy evaluation creation of Collection of pseudo-FieldOptimizations for generating
	 * xmlns: "attributes".
	 * 
	 * @return
	 */
	ArrayList<FieldToXMLOptimizations> xmlnsAttributeOptimizations()
	{
		ArrayList<FieldToXMLOptimizations>	result	= this.xmlnsAttributeOptimizations;
		if (result == null)
		{
			result		= new ArrayList<FieldToXMLOptimizations>(2);
			this.xmlnsAttributeOptimizations		= result;
		}
		return result;
	}
	boolean containsNameSpaceClass(String nsID)
	{
		return nameSpaceClassesById().containsKey(nsID);
	}
	Class<? extends ElementState> lookupNameSpaceClassById(String nsID)
	{
		return nameSpaceClassesById().get(nsID);
	}

	HashMap<String, Class<? extends ElementState>> nameSpaceClassesById()
	{
		HashMap<String, Class<? extends ElementState>> result = nameSpaceClassesById;
		if (result == null)
		{
			result					= new HashMap<String, Class<? extends ElementState>>(2);
			nameSpaceClassesById	= result;
		}
		return result;
	}
	class OptimizationsMap extends HashMapWriteSynch3<String, Class, ClassDescriptor>
	implements ValueFactory<Class, ClassDescriptor>
	{
		public ClassDescriptor getOrCreateAndPutIfNew(ElementState elementState)
		{
			return super.getOrCreateAndPutIfNew(elementState.getClass());
		}
		
		@Override
		protected String createKey(Class intermediate)
		{
			return intermediate.getName();
		}
		
		public ClassDescriptor createValue(Class key)
		{
			return new ClassDescriptor(key, parent);
		}
	}
	
	/**
	 * @return the Class Object that this holds Optimizations for.
	 */
	public Class thatClass()
	{
		return thatClass;
	}

	ElementDescriptor scalarTextN2jo()
	{
		return scalarTextN2jo;
	}

	public Field getScalarTextField()
	{
		return scalarTextField;
	}
	
	public boolean hasScalarTextField()
	{
		return scalarTextField != null;
	}
}
