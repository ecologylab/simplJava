package ecologylab.xml;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.AttributeType;

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
implements FieldTypes
{
	/**
	 * Class object that we are describing.
	 */
	private Class<? extends ElementState>					describedClass;
	
	private String																tagName;
	
	/**
	 * Map of FieldToXMLOptimizations, with field names as keys.
	 * 
	 * Used to optimize translateToXML(). 
	 * Also handy for providing functionality like associative arrays in Perl, JavaScript, PHP, ..., but with less overhead,
	 * because the hashtable is only maintained per class, not per instance.
	 */
	private HashMapArrayList<String, FieldDescriptor>	fieldDescriptorsByFieldName	= new HashMapArrayList<String, FieldDescriptor>();
	
	/**
	 * This data structure is handy for translateFromXML(). There can be multiple tags (keys in this map) for a single FieldDescriptor
	 * if @xml_other_tags is used.
	 */
	private HashMap<String, FieldDescriptor>		allFieldDescriptorsByTags					= new HashMap<String, FieldDescriptor>();
	
	private ArrayList<FieldDescriptor>					attributeFieldDescriptors					= new ArrayList<FieldDescriptor>();
	
	private ArrayList<FieldDescriptor>					elementFieldDescriptors						= new ArrayList<FieldDescriptor>();;
	
	/**
	 * The fields that are represented as attributes for the class we're optimizing.
	 */
	private ArrayList<Field>					attributeFields;	//TODO -- delete this
	
	private ArrayList<FieldToXMLOptimizations>	attributeFieldOptimizations;

	/**
	 * Map of NodeToJavaOptimizations. The keys are tag names.
	 * Used to optimize translateFromXML(...).
	 */
	private HashMap<String, TagDescriptor>	nodeToJavaOptimizationsMap	= new HashMap<String, TagDescriptor>();
	

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
	private TagDescriptor				scalarTextN2jo;
	
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
	 * This is a pseudo FieldDescriptor object, defined for the class, for cases in which the tag for
	 * the root element or a field is determined by class name, not by field name.
	 */
	private FieldDescriptor 			pseudoFieldDescriptor;
	
	
	private static final OptimizationsMap	allClassDescriptorsMap	= new OptimizationsMap();

	private boolean												isGetAndOrganizeComplete;
	
	/**
	 * Constructor is private, because values of this type are accessed through lazy evaluation, and cached.
	 * See also lookupRoot(), lookupChildOptimizations().
	 * @param thatClass
	 */
	private ClassDescriptor(Class<? extends ElementState> thatClass)
	{
		super();
		this.describedClass		= thatClass;
		this.tagName					= XMLTools.getXmlTagName(thatClass, TranslationScope.STATE);
	}

	public String getTagName()
	{
		return tagName;
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
	static ClassDescriptor getClassDescriptor(ElementState elementState)
	{
		Class<? extends ElementState> thatClass		= elementState.getClass();

		return getClassDescriptor(thatClass);
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
	public static ClassDescriptor getClassDescriptor(Class<? extends ElementState> thatClass)
	{
		String className	= thatClass.getName();
		// stay out of the synchronized block most of the time
		ClassDescriptor result= allClassDescriptorsMap.get(className);
		if (result == null || !result.isGetAndOrganizeComplete)
		{
			// but still be thread safe!
			synchronized (allClassDescriptorsMap)
			{
				result 		= allClassDescriptorsMap.get(className);
				if (result == null )
				{
					result				= new ClassDescriptor(thatClass);
					allClassDescriptorsMap.put(className, result);
					
					// NB: this call was moved out of the constructor to avoid recursion problems
					result.deriveAndOrganizeFieldsRecursive(thatClass);
					result.isGetAndOrganizeComplete	= true;
				}
				// THIS SHOULD NEVER HAPPEN!!!
				else if (!result.isGetAndOrganizeComplete)
				{
					if (!thatClass.equals(FieldDescriptor.class) && !thatClass.equals(ClassDescriptor.class))
						result.error("Horrible race condition that should never happen! See Andruid or Nabeel.");
				}
			}
		}
		return result;
	}
	
	/**
	 * Form a pseudo-FieldDescriptor-object for a root element.
	 * We say pseudo, because there is no Field corresponding to this element.
	 * The pseudo-FieldDescriptor-object still guides the translation process.
	 * 
	 * @return
	 */
	public FieldDescriptor pseudoFieldDescriptor()
	{
		FieldDescriptor result			= pseudoFieldDescriptor;
		if (result == null)
		{
			synchronized (this)
			{
				result													= pseudoFieldDescriptor;
				if (result == null)
				{
				    result 											= new FieldDescriptor(this);
 				    pseudoFieldDescriptor	= result;
				}
			}
		}
		return result;
	}


//	/**
//	 * @return Returns the nameSpacePrefix.
//	 */
//	String nameSpacePrefix()
//	{
//		return nameSpacePrefix;
//	}
//	/**
//	 * @param nameSpacePrefix The nameSpacePrefix to set.
//	 */
//	void setNameSpacePrefix(String nameSpacePrefix)
//	{
//		this.nameSpacePrefix = nameSpacePrefix;
//	}

	/**
	 * Get an object that describes how the field gets translated from XML.
	 * We cache these in the parseTable for each class, to optimize speed.
	 * <p/>
	 * These entries are created as needed, just-in-time, by lazy evaluation.
	 * @param translationSpace TODO
	 * @param context TODO
	 * @param node TODO
	 */
	TagDescriptor elementNodeToJavaOptimizations(TranslationScope translationSpace, ElementState context, Node node)
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
	TagDescriptor nodeToJavaOptimizations(TranslationScope translationSpace, ElementState context, String tag, boolean isAttribute)
	{
		TagDescriptor result	= nodeToJavaOptimizationsMap.get(tag);
		
		if (result == null)
		{
			synchronized (N2JO_TAG_FIELDS_LOCK)
			{
				result	= nodeToJavaOptimizationsMap.get(tag);
				
				if (setupTranslateFromXML(translationSpace, context))
					result	= nodeToJavaOptimizationsMap.get(tag);	// try again after setup
					
				if (result == null)
				{
					result	= new TagDescriptor(translationSpace, this, context, tag, isAttribute);
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
	
	public ArrayList<FieldDescriptor>	attributeFieldDescriptors()
	{
		return attributeFieldDescriptors;
	}
	
	public ArrayList<FieldDescriptor>	elementFieldOptimizations()
	{
		return elementFieldDescriptors;
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
		ClassDescriptor thatClassOptimizations	= getClassDescriptor(thatClass);
		
		HashMapArrayList<String, FieldDescriptor> result	= null;
		
		if (thatClass != null)
		{
			result								= thatClassOptimizations.getFieldDescriptorsForThis(fieldDescriptorClass);
		}
		return result;
	}

	static final Class[] NEW_FIELD_DESCRIPTOR_TYPES =
	{
		ClassDescriptor.class, FieldToXMLOptimizations.class,
	};
	
	private HashMapArrayList<String, FieldDescriptor> fieldDescriptorsByTagName;
	/**
	 * Build and return an ArrayList with Field objects for all the annotated fields in this class.
	 * 
	 * @param fieldDescriptorClass	The Class to use for instantiating each FieldDescriptor.
	 * 								The default is FieldDescriptor, but class objects may be passed in that extend that class.
	 * 
	 * @return	HashMapArrayList of Field objects, using the XML tag name for each field
	 * (not its Java field name!) as the keys. Could be empty. Never null.
	 */
	//FIXME -- MetaMetadata support!!! how do we pass in MetaMetadataFieldDescriptor class to instantiate during deriveAndOrganizeFieldsRecursive() ??????
	private HashMapArrayList<String, FieldDescriptor> createFieldDescriptors(Class<? extends FieldDescriptor> fieldDescriptorClass)
	{
		HashMapArrayList<String, FieldDescriptor> result		= fieldDescriptorsByTagName;
		if (result != null)
		{
				result	= new HashMapArrayList<String, FieldDescriptor>(fieldDescriptorsByFieldName.size());
				for (FieldDescriptor fieldDescriptor : fieldDescriptorsByFieldName)
					result.put(fieldDescriptor.getTagName(), fieldDescriptor);
				fieldDescriptorsByTagName	= result;
		}
		return result;
	}
	
	private FieldDescriptor createFieldDescriptor(Class<? extends FieldDescriptor> fieldDescriptorClass, FieldToXMLOptimizations attrF2XO)
	{
		Object[] args	= new Object[2];
		args[0]			= this;
		args[1]			= attrF2XO;
		
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

	private Class<FieldDescriptor> fieldDescriptorAnnotationValue(Class thatClass)
	{
		Annotation classDescriptorAnnotation	= thatClass.getAnnotation(xml_class_descriptor.class);
		xml_class_descriptor xmlClassDescriptor	= xml_class_descriptor.class.cast(classDescriptorAnnotation);
		Class<FieldDescriptor> result	= null;
		if (xmlClassDescriptor != null)
		{
			Class annotatedFieldDescriptorClass	= xmlClassDescriptor.value();
			if (annotatedFieldDescriptorClass != null && FieldDescriptor.class.isAssignableFrom(annotatedFieldDescriptorClass))
				result	= (Class<FieldDescriptor>) annotatedFieldDescriptorClass;
		}
		return result;
	}
	/**
	 * Recursive method to create optimized data structures needed for
	 * translation to and from XML, and also for efficient reflection-based access
	 * to field (descriptors) at run-time, with field name as a variable.
	 * <p/>
	 * Recurses up the chain of inherited Java classes, when @xml_inherit is specified.
	 */
	private synchronized void deriveAndOrganizeFieldsRecursive(Class thatClass)//, Class<FieldDescriptor> fieldDecriptorClass)
	{
		
		//Commented out by nabeel to solve the earlier error. Commenting of code includes the second parameter in the function call
		
//		if (thatClass.isAnnotationPresent(xml_inherit.class)) 
//		{	// recurse on super class first, so subclass declarations shadow those in superclasses, where there are field name conflicts
//			Class superClass	= thatClass.getSuperclass();
//			
//			if (fieldDecriptorClass == null)		
//			{	// look for annotation in super class if subclass didn't have one
//				fieldDecriptorClass	= fieldDescriptorAnnotationValue(thatClass);
//			}
//
//			if (superClass != null)
//				deriveAndOrganizeFieldsRecursive(superClass, fieldDecriptorClass);
//		}

		
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
			FieldDescriptor fieldDescriptor	= null;
			boolean					isElement				= true;
			//TODO -- if fieldDescriptorClass is already defined, then use it w reflection, instead of FieldDescriptor, itself
			if (XMLTools.representAsAttribute(thatField))
			{
				isElement					= false;
				fieldDescriptor		= new FieldDescriptor(this, thatField, ATTRIBUTE);
			}
			else if (XMLTools.representAsLeaf(thatField))
			{
				fieldDescriptor		= new FieldDescriptor(this, thatField, LEAF);
			}
			else if (XMLTools.representAsText(thatField))
			{
				fieldDescriptor		= new FieldDescriptor(this, thatField, TEXT_ELEMENT);
			}
			else if (XMLTools.representAsNested(thatField))
			{
				fieldDescriptor		= new FieldDescriptor(this, thatField, NESTED_ELEMENT);
			}
			else if (XMLTools.representAsCollection(thatField))
			{
				fieldDescriptor		= new FieldDescriptor(this, thatField, COLLECTION_ELEMENT);
			}
			else if (XMLTools.representAsMap(thatField))
			{
				fieldDescriptor		= new FieldDescriptor(this, thatField, MAP_ELEMENT);
			}
			else	// not an ecologylab.xml annotated field
				continue;
			
			if (isElement)
				elementFieldDescriptors.add(fieldDescriptor);
			else
				attributeFieldDescriptors.add(fieldDescriptor);
			
			//TODO -- throughout this block -- instead of just put, do contains() before put,
			// and generate a warning message if a mapping is being overridden
			fieldDescriptorsByFieldName.put(thatField.getName(), fieldDescriptor);
			if (!fieldDescriptor.isTagNameFromClassName())	// tag(s) from field, not from class :-)
			{
				String fieldTagName	= fieldDescriptor.getTagName();
				//TODO -- handle @xml_wrapped collections & maps
				if (fieldDescriptor.isCollection())
				{
					if (fieldDescriptor.isWrapped())
					{
						allFieldDescriptorsByTags.put(fieldTagName, fieldDescriptor);
						//TODO -- how do we keep state inside here to translateFromXML() the collection elements?
					}
					else
						allFieldDescriptorsByTags.put(fieldDescriptor.getCollectionOrMapTagName(), fieldDescriptor);
				}
				else // not collection
					allFieldDescriptorsByTags.put(fieldTagName, fieldDescriptor);
				// also add mappings for @xml_other_tags
				ElementState.xml_other_tags otherTagsAnnotation 	= thatField.getAnnotation(ElementState.xml_other_tags.class);
				String[] otherTags		= XMLTools.otherTags(otherTagsAnnotation);
				if (otherTags != null)
				{
					for (String otherTag : otherTags)
						allFieldDescriptorsByTags.put(otherTag, fieldDescriptor);
				}
			}
			else
			{	// add mappings by class tagNames
				for (ClassDescriptor classDescriptor: fieldDescriptor.getTagClassDescriptors())
				{
					FieldDescriptor pseudoFieldDescriptor	= classDescriptor.pseudoFieldDescriptor();
					fieldDescriptorsByFieldName.put(classDescriptor.tagName, pseudoFieldDescriptor);
				}
			}
			thatField.setAccessible(true);	// else -- ignore non-annotated fields
		}	// end for all fields
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
			TagDescriptor n2jo		= null;
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
	private TagDescriptor registerN2JOByClass(TranslationScope tspace, Field thatField,
			Class thatClass)
	{
		TagDescriptor n2jo	= 
			new TagDescriptor(tspace, this, thatField, thatClass);
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
	private TagDescriptor registerTagOptimizationsIfNeeded(boolean isAttribute, TranslationScope tspace, Field thatField, ElementState.xml_tag tagAnnotation)
	{
		String thatTag					= tagAnnotation.value();
		TagDescriptor	result	= null;
		if ((thatTag != null) && (thatTag.length() > 0))
		{
			result						= nodeToJavaOptimizationsMap.get(thatTag);
			if (result == null)
			{
				result					= new TagDescriptor(tspace, this, thatField, thatTag, isAttribute);
				nodeToJavaOptimizationsMap.put(thatTag, result);
			}
		}
		return result;
	}
	
	private boolean registerOtherTagsOptimizationsIfNeeded(TagDescriptor fieldN2jo, boolean isAttribute, TranslationScope tspace, Field thatField, ElementState.xml_other_tags otherTagsAnnotation)
	{
		String[] otherTags		= XMLTools.otherTags(otherTagsAnnotation);
		final boolean result	= (otherTags != null);
		if (result)
		{
			for (String otherTag : otherTags)
			{
				
				TagDescriptor n2jo	= nodeToJavaOptimizationsMap.get(otherTag);
				if (n2jo == null)
				{
					if (fieldN2jo != null)
						n2jo					= fieldN2jo;
					else
						n2jo					= new TagDescriptor(tspace, this, thatField, otherTag, isAttribute);
					nodeToJavaOptimizationsMap.put(otherTag, n2jo);
				}
			}
		}
		return result;
	}
	
	/**
	 * Add this Field to various data structures, and make it accessible for reflection.
	 * 
	 * @param fieldDescriptorsArrayList
	 * @param thatField
	 */
	//TODO -- get rid of either fieldsArrayList or isAttribute
	private FieldDescriptor formAndIndexFieldDesriptor(ArrayList<FieldDescriptor> fieldDescriptorsArrayList, Field thatField)
	{
		FieldDescriptor fieldDescriptor	= new FieldDescriptor(this, thatField, -99);
		mapFieldDescriptor(fieldDescriptor);
		fieldDescriptorsArrayList.add(fieldDescriptor);
		return fieldDescriptor;
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
	 * @param fieldDescriptor
	 */
	private void mapFieldDescriptor(FieldDescriptor fieldDescriptor)
	{
		//FIXME is tag determined by field by class?
		String tagName	= fieldDescriptor.getTagName();
		if (tagName != null)
			allFieldDescriptorsByTags.put(tagName, fieldDescriptor);
	}


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
		return "ClassDescriptor[" + describedClass.getName() + "]"; 
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
static class OptimizationsMap extends HashMapWriteSynch3<String, Class, ClassDescriptor>
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
			return new ClassDescriptor(key);
		}
	}
	
	/**
	 * @return the Class Object that this describes.
	 */
	public Class describedClass()
	{
		return describedClass;
	}

	TagDescriptor scalarTextN2jo()
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

	public Class<? extends ElementState> getDescribedClass() {
		return describedClass;
	}


}
