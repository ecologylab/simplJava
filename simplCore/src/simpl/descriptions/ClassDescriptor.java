package simpl.descriptions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_descriptor_classes;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_map;
import simpl.annotations.dbal.simpl_map_key_field;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_other_tags;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;
import simpl.annotations.dbal.simpl_use_equals_equals;
import simpl.core.SimplTypesScope;
import simpl.core.TranslationContext;
import simpl.descriptions.indexers.FieldDescriptorIndexer;
import simpl.deserialization.ISimplDeserializationHooks;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.StringFormat;
import simpl.platformspecifics.SimplPlatformSpecifics;
import simpl.tools.XMLTools;
import simpl.types.ScalarType;
import simpl.types.TypeRegistry;
import simpl.types.element.IMappable;

import ecologylab.generic.HashMapArrayList;
import ecologylab.generic.ReflectionTools;

/**
 * Cached object that holds all of the structures needed to optimize
 * translations to and from XML for a single subclass of ElementState. A
 * rootOptimizationsMap keeps track of these, using the XML tag as the key.
 * <p/>
 * This structure itself, as well as the structure within it, are created just
 * in time, by lazy evaluation.
 * 
 * @author andruid
 */
@simpl_inherit
public class ClassDescriptor<FD extends FieldDescriptor> extends DescriptorBase
		implements IMappable<String>, Iterable<FD>, ISimplDeserializationHooks, IClassDescriptor {

	public static interface FieldDescriptorsDerivedEventListener {
		void fieldDescriptorsDerived(Object... eventArgs);
	}
	


	private static final String PACKAGE_CLASS_SEP = ".";

	static {
		TypeRegistry.init();
	}
	
	/**
	 * Class object that we are describing.
	 */
	@simpl_scalar
	private Class<?> describedClass;

	@simpl_scalar
	private String describedClassSimpleName;

	@simpl_scalar
	private String describedClassPackageName;

	@simpl_composite
	private ClassDescriptor<? extends FieldDescriptor> superClass;

	@simpl_collection("interface")
	@simpl_other_tags("inerface")
	// handle spelling error that was here
	private ArrayList<String> interfaces;

	/**
	 * Class object that we are describing.
	 */
	@simpl_scalar
	private Class<? extends ClassDescriptor> classDescriptorClass;

	/**
	 * Class object that we are describing.
	 */
	@simpl_scalar
	private Class<? extends FieldDescriptor> fieldDescriptorClass;

	/**
	 * This flag prevents loops when creating descriptors for type graphs.
	 */
	boolean isGetAndOrganizeComplete;

	/**
	 * Map of FieldToXMLOptimizations, with field names as keys.
	 * 
	 * Used to optimize translateToXML(). Also handy for providing functionality
	 * like associative arrays in Perl, JavaScript, PHP, ..., but with less
	 * overhead, because the hashtable is only maintained per class, not per
	 * instance.
	 */
	private HashMapArrayList<String, FD> fieldDescriptorsByFieldName = new HashMapArrayList<String, FD>();

	@simpl_nowrap	
	@simpl_map("field_descriptor")
	@simpl_map_key_field("name")
	private HashMapArrayList<String, FD> declaredFieldDescriptorsByFieldName = new HashMapArrayList<String, FD>();

	
	public FieldDescriptorIndexer fieldDescriptors = new FieldDescriptorIndexer();
	/**
	 * This data structure is handy for translateFromXML(). There can be
	 * multiple tags (keys in this map) for a single FieldDescriptor if @simpl_other_tags
	 * is used.
	 */
	private HashMap<String, FD> allFieldDescriptorsByTagNames = new HashMap<String, FD>();

	private ArrayList<FD> attributeFieldDescriptors = new ArrayList<FD>();

	private ArrayList<FD> elementFieldDescriptors = new ArrayList<FD>();;

	private FD scalarValueFieldDescripotor = null;

	
	private ArrayList<FD> unresolvedScopeAnnotationFDs;

	private ArrayList<FD> unresolvedClassesAnnotationFDs;

	@simpl_collection("generic_type_variable")
	private ArrayList<String> genericTypeVariables = new ArrayList<String>();

	/**
	 * true if the class was annotated with @simpl_use_equals_equals, and thus
	 * that test will be used during de/serialization to detect equivalent
	 * objects
	 */
	@simpl_scalar
	private boolean strictObjectGraphRequired = false;

	public Class<?> fdClass;

	@simpl_collection("generic_type_var")
	private ArrayList<GenericTypeVar> genericTypeVars = null;

	private ArrayList<String> declaredGenericTypeVarNames = null;

	@simpl_collection("super_class_generic_type_var")
	private ArrayList<GenericTypeVar> superClassGenericTypeVars = null;

	@simpl_scalar
	private String explictObjectiveCTypeName;

	private List<FieldDescriptorsDerivedEventListener> fieldDescriptorsDerivedEventListeners;



	/**
	 * Default constructor only for use by translateFromXML().
	 */
	public ClassDescriptor() {
		super();
	}

	/**
	 * Constructor typically used by this class for creating a ClassDescriptor
	 * given its Java Class.
	 * 
	 * @param thatClass
	 */
	protected ClassDescriptor(Class<?> thatClass) {
		super(XMLTools.getXmlTagName(thatClass, "State"),
				thatClass.getName());

		this.describedClass = thatClass;
		this.describedClassSimpleName = thatClass.getSimpleName();
		this.describedClassPackageName = thatClass.getPackage().getName();

		final simpl_descriptor_classes descriptorsClassesAnnotation = thatClass
				.getAnnotation(simpl_descriptor_classes.class);
		if (descriptorsClassesAnnotation != null) {
			classDescriptorClass = (Class<? extends ClassDescriptor>) descriptorsClassesAnnotation
					.value()[0];
			fieldDescriptorClass = (Class<? extends FieldDescriptor>) descriptorsClassesAnnotation
					.value()[1];
		}

		if (thatClass.isAnnotationPresent(simpl_inherit.class))
			this.superClass = ClassDescriptors.getClassDescriptor(thatClass.getSuperclass());

		addGenericTypeVariables();
		if (javaParser != null) {
			comment = javaParser.getJavaDocComment(thatClass);
		}
		if (thatClass.isAnnotationPresent(simpl_use_equals_equals.class)) {
			this.strictObjectGraphRequired = true;
		}
	}

	/**
	 * Constructor used by Meta-Metadata Compiler.
	 * 
	 * @param tagName
	 * @param comment
	 * @param describedClassPackageName
	 * @param describedClassSimpleName
	 * @param superClass
	 * @param interfaces
	 */
	protected ClassDescriptor(String tagName, String comment,
			String describedClassPackageName, String describedClassSimpleName,
			ClassDescriptor<FD> superClass, ArrayList<String> interfaces) {
		super(tagName, describedClassPackageName + PACKAGE_CLASS_SEP
				+ describedClassSimpleName, comment);

		this.describedClassPackageName = describedClassPackageName;
		this.describedClassSimpleName = describedClassSimpleName;
		this.superClass = superClass;
		this.interfaces = interfaces;
	}

	/**
	 * Handles a text node.
	 */
	private FieldDescriptor scalarTextFD;

	private Object SCOPE_ANNOTATION_LOCK = new Object();

	public FieldDescriptor getScalarTextFD() {
		return scalarTextFD;
	}

	void setScalarTextFD(FieldDescriptor scalarTextFD) {
		this.scalarTextFD = scalarTextFD;
	}

	/**
	 * Determines if this Class Descriptor handles a text node.
	 * @return Returns true if this node is a text node.
	 */
	public boolean hasScalarFD() {
		return scalarTextFD != null;
	}

	/**
	 * Returns a list of interfaces that this class implements
	 * @return
	 */
	public ArrayList<String> getInterfaceList() {
		return interfaces;
	}

	private void addGenericTypeVariables() {
		TypeVariable<?>[] typeVariables = describedClass.getTypeParameters();
		if (typeVariables != null && typeVariables.length > 0) {
			for (TypeVariable<?> typeVariable : typeVariables) {
				String typeClassName = typeVariable.getName();
				genericTypeVariables.add(typeClassName);
			}
		}
	}

	/**
	 * lazy-evaluation method.
	 * 
	 * @return
	 */
	public ArrayList<GenericTypeVar> getGenericTypeVars() {
		if (genericTypeVars == null) {
			synchronized (this) {
				if (genericTypeVars == null) {
					genericTypeVars = new ArrayList<GenericTypeVar>();
					deriveGenericTypeVariables();
				}
			}
		}

		return genericTypeVars;
	}

	/**
	 * lazy-evaluation method.
	 * 
	 * @return
	 */
	public ArrayList<GenericTypeVar> getSuperClassGenericTypeVars() {
		if (superClassGenericTypeVars == null) {
			synchronized (this) {
				if (superClassGenericTypeVars == null) {
					// superClassGenericTypeVars = new
					// ArrayList<GenericTypeVar>();
					deriveSuperGenericTypeVariables();
				}
			}
		}

		return superClassGenericTypeVars;
	}

	// added a setter to enable environment specific implementation -Fei
	public void setSuperClassGenericTypeVars(
			ArrayList<GenericTypeVar> derivedSuperClassGenericTypeVars) {
		synchronized (this) {
			superClassGenericTypeVars = derivedSuperClassGenericTypeVars;
		}
	}

	// This method is modified, refer to FundamentalPlatformSpecific package
	// -Fei
	private void deriveSuperGenericTypeVariables() {
		SimplPlatformSpecifics.get()
				.deriveSuperClassGenericTypeVars(this);
	}

	private void deriveGenericTypeVariables() {
		if (describedClass != null) // for generated descriptors, describedClass
									// == null
		{
			TypeVariable<?>[] typeVariables = describedClass
					.getTypeParameters();
			if (typeVariables != null && typeVariables.length > 0) {
				for (TypeVariable<?> typeVariable : typeVariables) {
					GenericTypeVar g = GenericTypeVar.getGenericTypeVarDef(
							typeVariable, this.genericTypeVars);
					this.genericTypeVars.add(g);
				}
			}
		}
	}

	@Override
	public String getTagName() {
		return tagName;
	}

	private List<FieldDescriptorsDerivedEventListener> fieldDescriptorsDerivedEventListeners() {
		if (fieldDescriptorsDerivedEventListeners == null)
			this.fieldDescriptorsDerivedEventListeners = new ArrayList<FieldDescriptorsDerivedEventListener>();
		return fieldDescriptorsDerivedEventListeners;
	}

	void addFieldDescriptorDerivedEventListener(
			FieldDescriptorsDerivedEventListener listener) {
		fieldDescriptorsDerivedEventListeners().add(listener);
	}

	void handleFieldDescriptorsDerivedEvent() {
		if (fieldDescriptorsDerivedEventListeners != null) {
			for (FieldDescriptorsDerivedEventListener listener : fieldDescriptorsDerivedEventListeners) {
				listener.fieldDescriptorsDerived();
			}
			fieldDescriptorsDerivedEventListeners.clear();
		}
	}

	

	public ArrayList<FD> allFieldDescriptors() {
		ArrayList<FD> allFieldDescriptors = new ArrayList<FD>();
		if (attributeFieldDescriptors != null)
			allFieldDescriptors.addAll(attributeFieldDescriptors);
		if (elementFieldDescriptors != null)
			allFieldDescriptors.addAll(elementFieldDescriptors);
		return allFieldDescriptors;
	}

	public ArrayList<FD> attributeFieldDescriptors() {
		return attributeFieldDescriptors;
	}

	public ArrayList<FD> elementFieldDescriptors() {
		return elementFieldDescriptors;
	}

	@Override
	public Iterator<FD> iterator() {
		return fieldDescriptorsByFieldName.iterator();
	}

	/**
	 * Recursive method to create optimized data structures needed for
	 * translation to and from XML, and also for efficient reflection-based
	 * access to field (descriptors) at run-time, with field name as a variable.
	 * <p/>
	 * Recurses up the chain of inherited Java classes, when @xml_inherit is
	 * specified.
	 * 
	 * @param fdc
	 * @return
	 */
	synchronized void deriveAndOrganizeFieldsRecursive(
			Class<? extends Object> classWithFields) {
		if (classWithFields.isAnnotationPresent(simpl_inherit.class))

		{
			ClassDescriptor<FD> superClassDescriptor = (ClassDescriptor<FD>) ClassDescriptors
					.getClassDescriptor(classWithFields.getSuperclass());

			referFieldDescriptorsFrom(superClassDescriptor);
		}

		Field[] fields = classWithFields.getDeclaredFields();

		FieldCategorizer fc = new FieldCategorizer();
		
		for (int i = 0; i < fields.length; i++) {
			Field thatField = fields[i];

			FieldType fieldType = fc.categorizeField(thatField);
			
			if (fieldType == FieldType.UNSET_TYPE)
				continue; // not a simpl serialization annotated field

			FD fieldDescriptor = newFieldDescriptor(thatField, fieldType,
					(Class<FD>) fieldDescriptorClass);
			
			if (fieldDescriptor != null) 
			{
				fieldDescriptor.genericTypeVarsContextCD = this;
			}
			
			// create indexes for serialize
			if (fieldDescriptor.getType() == FieldType.SCALAR) {
				Hint xmlHint = fieldDescriptor.getXmlHint();
				switch (xmlHint) {
				case XML_ATTRIBUTE:
					attributeFieldDescriptors.add(fieldDescriptor); // todo: reference this all via an indexer. :) 
					break;
				case XML_TEXT:
				case XML_TEXT_CDATA:
					break;
				case XML_LEAF:
				case XML_LEAF_CDATA:
					elementFieldDescriptors.add(fieldDescriptor);
					break;
				}
			} else {
				elementFieldDescriptors.add(fieldDescriptor);
			}

			if (FieldCategorizer.isCompositeAsScalarvalue(thatField)) {
				scalarValueFieldDescripotor = fieldDescriptor;
			}

			// generate a warning message if a mapping is being overridden
			fieldDescriptorsByFieldName.put(thatField.getName(),
					fieldDescriptor);
			if (classWithFields == describedClass) {
				declaredFieldDescriptorsByFieldName.put(thatField.getName(),
						fieldDescriptor);
			}

			// create mappings for translateFromXML() -->
			// allFieldDescriptorsByTagNames
			final String fieldTagName = fieldDescriptor.getTagName();
			if (fieldDescriptor.isWrapped()) {

				FD wrapper = newFieldDescriptor(fieldDescriptor, fieldTagName,
						(Class<FD>) fieldDescriptorClass);
				mapTagToFdForDeserialize(fieldTagName, wrapper);
				mapOtherTagsToFdForDeserialize(wrapper,
						fieldDescriptor.otherTags());
			} else if (!fieldDescriptor.isPolymorphic()) // tag(s) from field,
															// not from class
															// :-)
			{

				String tag = null;
				if (fieldDescriptor.isCollection()) {
					
					tag = fieldDescriptor.getCollectionOrMapTagName();
				
				} else {
					tag = fieldTagName;
				}
				
				if(tag == null) 
				{
					throw new RuntimeException("Tag should never be null! Fix it!");
				}

				mapTagToFdForDeserialize(tag, fieldDescriptor);
				mapOtherTagsToFdForDeserialize(fieldDescriptor,
						fieldDescriptor.otherTags());
			} else {
				mapPolymorphicClassDescriptors(fieldDescriptor);
			}
			thatField.setAccessible(true); // else -- ignore non-annotated
											// fields
		} // end for all fields
	}

	private void referFieldDescriptorsFrom(
			ClassDescriptor<FD> superClassDescriptor) {
		initDeclaredGenericTypeVarNames();

		Map<FieldDescriptor, FieldDescriptor> bookkeeper = new HashMap<FieldDescriptor, FieldDescriptor>();

		for (Entry<String, FD> fieldDescriptorEntry : superClassDescriptor
				.getFieldDescriptorsByFieldName().entrySet()) {
			fieldDescriptorsByFieldName.put(
					fieldDescriptorEntry.getKey(),
					perhapsCloneGenericField(fieldDescriptorEntry.getValue(),
							bookkeeper));
		}

		for (Entry<String, FD> fieldDescriptorEntry : superClassDescriptor
				.getDeclaredFieldDescriptorsByFieldName().entrySet()) {
			declaredFieldDescriptorsByFieldName.put(
					fieldDescriptorEntry.getKey(),
					perhapsCloneGenericField(fieldDescriptorEntry.getValue(),
							bookkeeper));
		}

		for (Entry<String, FD> fieldDescriptorEntry : superClassDescriptor
				.getAllFieldDescriptorsByTagNames().entrySet()) {
			allFieldDescriptorsByTagNames.put(
					fieldDescriptorEntry.getKey(),
					perhapsCloneGenericField(fieldDescriptorEntry.getValue(),
							bookkeeper));
		}


		for (FD fieldDescriptor : superClassDescriptor
				.attributeFieldDescriptors()) {
			attributeFieldDescriptors.add(perhapsCloneGenericField(
					fieldDescriptor, bookkeeper));
		}

		for (FD fieldDescriptor : superClassDescriptor
				.elementFieldDescriptors()) {
			elementFieldDescriptors.add(perhapsCloneGenericField(
					fieldDescriptor, bookkeeper));
		}

		FieldDescriptor scalarTextFD = superClassDescriptor.getScalarTextFD();
		if (scalarTextFD != null) { // added by Zach -- doesn't seem to be
									// covered otherwise
			this.setScalarTextFD(perhapsCloneGenericField(scalarTextFD,
					bookkeeper));
		}

		if (superClassDescriptor.getUnresolvedScopeAnnotationFDs() != null) {
			for (FD fd : superClassDescriptor.getUnresolvedScopeAnnotationFDs()) {
				this.registerUnresolvedScopeAnnotationFD(perhapsCloneGenericField(
						fd, bookkeeper));
			}
		}

		if (superClassDescriptor.getUnresolvedClassesAnnotationFDs() != null) {
			for (FD fd : superClassDescriptor
					.getUnresolvedClassesAnnotationFDs()) {
				this.registerUnresolvedClassesAnnotationFD(perhapsCloneGenericField(
						fd, bookkeeper));
			}
		}
	}

	private void initDeclaredGenericTypeVarNames() {
		if (declaredGenericTypeVarNames == null && describedClass != null) {
			ArrayList<String> result = new ArrayList<String>();
			TypeVariable<?>[] typeParams = describedClass.getTypeParameters();
			if (typeParams != null && typeParams.length > 0) {
				for (TypeVariable<?> typeParam : typeParams)
					result.add(typeParam.getName());
			}
			if (result.size() > 0)
				declaredGenericTypeVarNames = result;
		}
	}

	private <FDT extends FieldDescriptor> FDT perhapsCloneGenericField(FDT fd,
			Map<FieldDescriptor, FieldDescriptor> bookkeeper) {
		if (declaredGenericTypeVarNames == null || fd.field == null) {
			return fd;
		}

		if (bookkeeper.containsKey(fd)) {
			return (FDT) bookkeeper.get(fd);
		}

		FDT result = fd;
		Type genericType = fd.field.getGenericType();
		if (isTypeUsingGenericNames(genericType, declaredGenericTypeVarNames)) {
			result = (FDT) new FieldDescriptor();
			result.setGenericTypeVars(null);
			result.genericTypeVarsContextCD = this;
		}
		bookkeeper.put(fd, result);
		return result;
	}

	private boolean isTypeUsingGenericNames(Type genericType,
			ArrayList<String> names) {
		if (genericType != null) {
			if (genericType instanceof TypeVariable) {
				TypeVariable tv = (TypeVariable) genericType;
				if (names.contains(tv.getName()) || tv.getBounds().length > 0
						&& isTypeUsingGenericNames(tv.getBounds()[0], names)) {
					return true;
				}
			} else if (genericType instanceof WildcardType) {
				WildcardType wt = (WildcardType) genericType;
				if (wt.getUpperBounds().length > 0
						&& isTypeUsingGenericNames(wt.getUpperBounds()[0],
								names)) {
					return true;
				}
			} else if (genericType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericType;
				Type[] args = pt.getActualTypeArguments();
				for (Type arg : args) {
					if (isTypeUsingGenericNames(arg, names)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	protected void mapOtherTagsToFdForDeserialize(FD fieldDescriptor,
			ArrayList<String> otherTags) {
		if (otherTags != null) {
			for (String otherTag : otherTags) {
				mapTagToFdForDeserialize(otherTag, fieldDescriptor);
			}
		}
	}

	/**
	 * @param fieldDescriptor
	 */
	void mapPolymorphicClassDescriptors(FD fieldDescriptor) {
		Collection<String> tagClassDescriptors = fieldDescriptor
				.getPolymorphicTags();

		if (tagClassDescriptors != null) {
			for (String tagName : tagClassDescriptors) {
				mapTagToFdForDeserialize(tagName, fieldDescriptor);
			}
		}

		mapTagToFdForDeserialize(fieldDescriptor.getTagName(), fieldDescriptor);
	}

	static final Class[] FIELD_DESCRIPTOR_ARGS = { ClassDescriptor.class,
			Field.class, FieldType.class };

	/**
	 * @param thatField
	 * @param fieldDescriptorClass
	 * @return
	 */
	private FD newFieldDescriptor(Field thatField, FieldType annotationType,
			Class<FD> fieldDescriptorClass) {
		if (fieldDescriptorClass == null) {
			return (FD) new FieldDescriptor(this, thatField, annotationType);
		}

		Object args[] = new Object[3];
		args[0] = this;
		args[1] = thatField;
		args[2] = annotationType;

		return ReflectionTools.getInstance(fieldDescriptorClass,
				FIELD_DESCRIPTOR_ARGS, args);
	}

	static final Class[] WRAPPER_FIELD_DESCRIPTOR_ARGS = {
			ClassDescriptor.class, FieldDescriptor.class, String.class };

	private FD newFieldDescriptor(FD wrappedFD, String wrapperTag,
			Class<FD> fieldDescriptorClass) {
		if (fieldDescriptorClass == null) {
			return (FD) new FieldDescriptor(this, wrappedFD, wrapperTag);
		}

		Object args[] = new Object[3];
		args[0] = this;
		args[1] = wrappedFD;
		args[2] = wrapperTag;

		return ReflectionTools.getInstance(fieldDescriptorClass,
				WRAPPER_FIELD_DESCRIPTOR_ARGS, args);
	}

	/**
	 * Map the tag to the FieldDescriptor for use in translateFromXML() for
	 * elements of this class type.
	 * 
	 * @param tagName
	 * @param fdToMap
	 */
	private void mapTagToFdForDeserialize(String tagName, FD fdToMap) {

		if (!fdToMap.isWrapped()) {
			FD previousMapping = allFieldDescriptorsByTagNames.put(tagName,
					fdToMap);
		
			if (previousMapping != null && previousMapping != fdToMap) {
			//	warning(" tag <" + tagName + ">:\tfield[" + fdToMap.getName()
				//		+ "] overrides field[" + previousMapping.getName()
					//	+ "]");
			}
		}
	}

	/**
	 * Add an entry to our map of Field objects, using the field's name as the
	 * key. Used, for example, for ignored fields.
	 * 
	 * @param fieldDescriptor
	 */
	void addFieldDescriptorMapping(FD fieldDescriptor) {
		String tagName = fieldDescriptor.getTagName();
		if (tagName != null) {
			mapTagToFdForDeserialize(tagName, fieldDescriptor);
		}
	}

	/**
	 * (used by the compiler)
	 * 
	 * @param fieldDescriptor
	 */
	protected void addFieldDescriptor(FD fieldDescriptor) {
		declaredFieldDescriptorsByFieldName.put(fieldDescriptor.getName(),
				fieldDescriptor);
	}

	@Override
	public String toString() {
		return this.name;//getClassSimpleName() + "[" + this.name + "]";
	}

	public Class<?> getDescribedClass() {
		return describedClass;
	}

	/**
	 * 
	 * @return true if this is an empty entry, for a tag that we do not parse.
	 *         No class is associated with such an entry.
	 */
	public boolean isEmpty() {
		return describedClass == null;
	}

	public String getDescribedClassSimpleName() {
		return describedClassSimpleName;
	}

	public String getDescribedClassPackageName() {
		return describedClassPackageName;
	}

	/**
	 * Get the full name of the class that this describes. Use the Class to get
	 * this, if there is one; else use de/serialize fields that describe this.
	 * 
	 * @return
	 */
	public String getDescribedClassName() {
		return getName();
	}

	/**
	 * @return The full, qualified name of the class that this describes.
	 */
	public String getJavaTypeName() {
		return getDescribedClassName();
	}
	
	// TODO: Make it default all scalar values. 
	public Object getInstance() throws SIMPLTranslationException {
		Object ourObject = XMLTools.getInstance(describedClass);
		return ourObject;
	}

	public int numFields() {
		return allFieldDescriptorsByTagNames.size();
	}

	/**
	 * The tagName.
	 */
	@Override
	public String key() {
		return tagName;
	}

	public HashMapArrayList<String, FD> getFieldDescriptorsByFieldName() {
		return fieldDescriptorsByFieldName;
	}

	public HashMapArrayList<String, FD> getDeclaredFieldDescriptorsByFieldName() {
		return declaredFieldDescriptorsByFieldName;
	}

	public HashMap<String, FD> getAllFieldDescriptorsByTagNames() {
		return allFieldDescriptorsByTagNames;
	}

	public ArrayList<FD> getUnresolvedScopeAnnotationFDs() {
		return this.unresolvedScopeAnnotationFDs;
	}

	public ArrayList<FD> getUnresolvedClassesAnnotationFDs() {
		return this.unresolvedClassesAnnotationFDs;
	}

	public String getSuperClassName() {
		return XMLTools.getClassSimpleName(describedClass.getSuperclass());
	}

	/**
	 * Keep track of any FieldDescriptors with unresolved @serial_scope
	 * declarations so we can try to resolve them later when there is use.
	 * 
	 * @param fd
	 */
	void registerUnresolvedScopeAnnotationFD(FD fd) {
		if (unresolvedScopeAnnotationFDs == null) {
			synchronized (this) {
				if (unresolvedScopeAnnotationFDs == null)
					unresolvedScopeAnnotationFDs = new ArrayList<FD>();
			}
		}
		unresolvedScopeAnnotationFDs.add(fd);
	}

	void registerUnresolvedClassesAnnotationFD(FD fd) {
		if (unresolvedClassesAnnotationFDs == null) {
			synchronized (this) {
				if (unresolvedClassesAnnotationFDs == null)
					unresolvedClassesAnnotationFDs = new ArrayList<FD>();
			}
		}
		unresolvedClassesAnnotationFDs.add(fd);
	}

	/**
	 * Late evaluation of @serial_scope, if it failed the first time around.
	 */
	public void resolvePolymorphicAnnotations() {
		resolveUnresolvedScopeAnnotationFDs();
		resolveUnresolvedClassesAnnotationFDs();
	}

	public void resolveUnresolvedScopeAnnotationFDs() {
		if (unresolvedScopeAnnotationFDs != null) {
			synchronized (SCOPE_ANNOTATION_LOCK) {
				if (unresolvedScopeAnnotationFDs != null) {
					for (int i = unresolvedScopeAnnotationFDs.size() - 1; i >= 0; i--) {
						FieldDescriptor fd = unresolvedScopeAnnotationFDs
								.remove(i);
						fd.resolveUnresolvedScopeAnnotation();
						this.mapPolymorphicClassDescriptors((FD) fd);
					}
					unresolvedScopeAnnotationFDs = null;
				}
			}
		}
	}

	/**	
	 * Late evaluation of @serial_scope, if it failed the first time around.
	 */
	public void resolveUnresolvedClassesAnnotationFDs() {
		if (unresolvedClassesAnnotationFDs != null) {
			for (int i = unresolvedClassesAnnotationFDs.size() - 1; i >= 0; i--) {
				FieldDescriptor fd = unresolvedClassesAnnotationFDs.remove(i);
				fd.resolveUnresolvedClassesAnnotation();
				this.mapPolymorphicClassDescriptors((FD) fd);
				this.mapPolymorphicClassDescriptors((FD) fd);
			}
		}
		unresolvedClassesAnnotationFDs = null;
	}

	/**
	 * Use the @simpl_other_tags annotation to obtain an array of alternative
	 * (old) tags for this class.
	 * 
	 * @return The array of old tags, or null, if there is no @simpl_other_tags
	 *         annotation.
	 */
	@Override
	public ArrayList<String> otherTags() {
		ArrayList<String> result = this.otherTags;
		if (result == null) {
			result = new ArrayList<String>();

			Class<?> thisClass = getDescribedClass();
			if (thisClass != null) {
				final simpl_other_tags otherTagsAnnotation = thisClass
						.getAnnotation(simpl_other_tags.class);

				// commented out since getAnnotation also includes inherited
				// annotations
				// ElementState.xml_other_tags otherTagsAnnotation =
				// thisClass.getAnnotation(ElementState.xml_other_tags.class);
				if (otherTagsAnnotation != null)
					for (String otherTag : otherTagsAnnotation.value())
						result.add(otherTag);
			}

			this.otherTags = result;
		}
		return result;
	}

	public FD getScalarValueFieldDescripotor() {
		return scalarValueFieldDescripotor;
	}

	public ClassDescriptor<? extends FieldDescriptor> getSuperClass() {
		return superClass;
	}

	/**
	 * method returns whether a strict object graph is required
	 * 
	 * @return true if the class was annotated with @simpl_use_equals_equals,
	 *         and thus that test will be used during de/serialization to detect
	 *         equivalent objects
	 */
	public boolean getStrictObjectGraphRequired() {
		return this.strictObjectGraphRequired;
	}



	
	public ScalarType getScalarType()
	{
		return null;
	}

	@Override
	public void deserializationPreHook(TranslationContext translationContext) {
		synchronized (ClassDescriptors.getGlobalDescriptorMap()) {
			String name = this.getName();
			if (name != null) {
				if (ClassDescriptors.getGlobalDescriptorMap().containsKey(name))
				{
					//error("Already a ClassDescriptor for " + name);
			}else {
				ClassDescriptors.getGlobalDescriptorMap().put(name, this);
				}
			}
		}
	}

	/**
	 * @return The list of meta-information (annotations, attributes, etc.) for
	 *         this class.
	 */
	public List<MetaInformation> getMetaInformation() {
		if (metaInfo == null) {
			metaInfo = new ArrayList<MetaInformation>();

			// @simpl_inherit
			if (superClass != null)
				metaInfo.add(new MetaInformation(simpl_inherit.class));

			// @simpl_tag
			String autoTagName = XMLTools.getXmlTagName(
					getDescribedClassSimpleName(), null);
			if (tagName != null && !tagName.equals("")
					&& !tagName.equals(autoTagName))
				metaInfo.add(new MetaInformation(simpl_tag.class, false,
						tagName));

			// @simpl_other_tags
			ArrayList<String> otherTags = otherTags();
			if (otherTags != null && otherTags.size() > 0)
				metaInfo.add(new MetaInformation(simpl_other_tags.class, true,
						otherTags.toArray()));
		}
		return metaInfo;
	}

	public void setDescribedClassSimpleName(String describedClassSimpleName) {
		this.describedClassSimpleName = describedClassSimpleName;
		this.tagName = XMLTools.getXmlTagName(describedClassSimpleName, null);
	}

	public void setDescribedClassPackageName(String describedClassPackageName) {
		this.describedClassPackageName = describedClassPackageName;
	}

	@Override
	public void deserializationInHook(TranslationContext translationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deserializationPostHook(TranslationContext translationContext,
			Object object) {
		// TODO Auto-generated method stub
		
	}



}
