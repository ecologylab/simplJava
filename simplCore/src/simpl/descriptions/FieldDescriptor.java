/**
 * 
 */
package simpl.descriptions;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_classes;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_filter;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_map;
import simpl.annotations.dbal.simpl_map_key_field;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_other_tags;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_scope;
import simpl.annotations.dbal.simpl_tag;
import simpl.annotations.dbal.simpl_wrap;
import simpl.core.ElementState;
import simpl.core.ScalarUnmarshallingContext;
import simpl.core.SimplTypesScope;
import simpl.core.TranslationContext;
import simpl.descriptions.MetaInformation.Argument;
import simpl.exceptions.SIMPLDescriptionException;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;
import simpl.formats.string.FormatRegistry;
import simpl.platformspecifics.SimplPlatformSpecifics;
import simpl.tools.XMLTools;
import simpl.types.FundamentalTypes;
import simpl.types.ScalarType;
import simpl.types.TypeRegistry;
import simpl.types.element.IMappable;

import ecologylab.generic.HashMapArrayList;
import ecologylab.generic.ReflectionTools;
import ecologylab.generic.StringBuilderBaseUtils;
import ecologylab.generic.StringTools;

/**
 * Used to provide convenient access for setting and getting values, using the
 * ecologylab.serialization type system. Provides marshalling and unmarshalling from Strings.
 * 
 * @author andruid
 */
@SuppressWarnings("rawtypes")
@simpl_inherit
public class FieldDescriptor extends DescriptorBase implements IMappable<String>
{

	public static final String NULL = "null"; // This is wrong; used to be "null" from SCalarType.DefaultValue...

	public static final Class[] SET_METHOD_STRING_ARG = { String.class };

	@simpl_scalar
	protected Field field;
	// TODO

	/**
	 * For nested elements, and collections or maps of nested elements. The class descriptor
	 */

	@simpl_composite
	private ClassDescriptor elementClassDescriptor;

	@simpl_scalar
	private String mapKeyFieldName;

	@simpl_scalar
	/**
	 * Represents the simple name of the field type.
	 */
	private String fieldTypeSimpleName;
	
	/**
	 * Descriptor for the class that this field is declared in.
	 */
	@simpl_composite
	protected ClassDescriptor declaringClassDescriptor;

	@simpl_scalar
	private Class elementClass;

	@simpl_scalar
	protected boolean isGeneric;

	/**
	 * For composite or collection fields declared with generic type variables, this field stores the
	 * binding to the resolved generic type from the ClassDescriptor.
	 * <p/>
	 * Note: this will require cloning this during inheritance, when subtypes instantiate the generic
	 * type var(s) with different values.
	 */
	@simpl_collection("generic_type_var")
	private ArrayList<GenericTypeVar> genericTypeVars;
	
	ClassDescriptor genericTypeVarsContextCD;

	// ///////////////// next fields are for polymorphic fields
	// ////////////////////////////////////////
	/**
	 * Null if the tag for this field is derived from its field declaration. For most fields, tag is
	 * derived from the field declaration (using field name or @simpl_tag).
	 * <p/>
	 * However, for polymorphic fields, such as those declared using @xml_class, @xml_classes, or
	 * 
	 * @xml_scope, the tag is derived from the class declaration (using class name or @simpl_tag).
	 *             This is, for example, required for polymorphic nested and collection fields. For
	 *             these fields, this slot contains an array of the legal classes, which will be bound
	 *             to this field during translateFromXML().
	 */
	@simpl_map("polymorph_class_descriptor")
	@simpl_map_key_field("tagName")
	private HashMapArrayList<String, ClassDescriptor>	polymorphClassDescriptors;

	@simpl_map("polymorph_class")
	private HashMap<String, Class>										polymorphClasses;

	@simpl_scalar
	private FieldType type;

	/**
	 * This slot makes sense only for attributes and leaf nodes
	 */
	@simpl_scalar
	private ScalarType scalarType;

	@simpl_scalar
	private Hint xmlHint;

	@simpl_scalar
	private boolean	isEnum;
	
	private EnumerationDescriptor enumDescriptor; 

	/**
	 * An option for scalar formatting.
	 */
	private String[] format;

	@simpl_scalar
	private boolean isCDATA;

	@simpl_scalar
	private boolean needsEscaping;

	@simpl_scalar
	Pattern filterRegex;
	
	@simpl_scalar
	int filterGroup;

	@simpl_scalar
	String filterReplace;

	/**
	 * The FieldDescriptor for the field in a wrap.
	 */
	private FieldDescriptor wrappedFD;
	
	private FieldDescriptor	wrapper;

	private HashMap<Integer, ClassDescriptor>					tlvClassDescriptors;

	private String unresolvedScopeAnnotation		= null;

	private Class[] unresolvedClassesAnnotation	= null;

	/**
 * 
 */
	@simpl_scalar
	private String collectionOrMapTagName;

	@simpl_scalar
	private String compositeTagName;

	/**
	 * Used for Collection and Map fields. Tells if the XML should be wrapped by an intermediate
	 * element.
	 */
	@simpl_scalar
	private boolean	wrapped;

	private Method	setValueMethod;

	@simpl_scalar
	private String fieldType;

	protected String genericParametersString;

	private ArrayList<ClassDescriptor> dependencies	= new ArrayList<ClassDescriptor>();
	
	/**
	 * if is null, this field is not a cloned one. <br />
	 * if not null, refers to the descriptor that this field is cloned from.
	 */
	private FieldDescriptor	clonedFrom;

	/**
	 * Default constructor only for use by translateFromXML().
	 */
	public FieldDescriptor()
	{
		super();
	}

	/**
	 * Constructor for the pseudo-FieldDescriptor associated with each ClassDesctiptor, for
	 * translateToXML of fields that deriveTagFromClass.
	 * 
	 * @param baseClassDescriptor
	 */
	public FieldDescriptor(ClassDescriptor baseClassDescriptor)
	{
		super(baseClassDescriptor.getTagName(), null);
		this.declaringClassDescriptor = baseClassDescriptor;
		this.field = null;
		this.type = FieldType.PSEUDO_FIELD_DESCRIPTOR;
		this.scalarType = null;
	}

	/**
	 * Constructor for wrapper FieldDescriptor. (Seems to not have a name; i guess the constituent
	 * field inside the wrapper is where the name is.)
	 * 
	 * @param baseClassDescriptor
	 * @param wrappedFD
	 * @param wrapperTag
	 */
	public FieldDescriptor(ClassDescriptor baseClassDescriptor, FieldDescriptor wrappedFD,
			String wrapperTag)
	{
		super(wrapperTag, null);
		this.declaringClassDescriptor = baseClassDescriptor;
		this.wrappedFD = wrappedFD;
		wrappedFD.wrapper = this;
		this.setType(FieldType.WRAPPER);
	}

	/**
	 * This is the normal constructor.
	 * 
	 * @param declaringClassDescriptor
	 * @param field
	 * @param annotationType
	 *          Coarse pre-evaluation of the field's annotation type. Does not differentiate scalars
	 *          from elements, or check for semantic consistency.
	 */
	public FieldDescriptor(ClassDescriptor declaringClassDescriptor, Field field, FieldType annotationType) // String
	// nameSpacePrefix
	{
		super(XMLTools.getXmlTagName(field), field.getName()); // uses field name or @simpl_tag
		// declaration
		this.declaringClassDescriptor = declaringClassDescriptor;
		this.field = field;
		this.field.setAccessible(true);
		this.fieldType = field.getType().getSimpleName();
		
		if (field.isAnnotationPresent(simpl_map_key_field.class))
		{
			this.mapKeyFieldName = field.getAnnotation(simpl_map_key_field.class).value();
		}
		
		// this.name = (field != null) ? field.getName() : "NULL";

		derivePolymorphicDescriptors(field);

		this.setType(FieldType.UNSET_TYPE); // for debugging!

		if (annotationType == FieldType.SCALAR)
		{
			type = deriveScalarSerialization(field);
		}
		else
		{
			type = deriveNestedSerialization(field, annotationType);
		}

		String fieldName = field.getName();
		StringBuilder capFieldName = new StringBuilder(fieldName);
		capFieldName.setCharAt(0, Character.toUpperCase(capFieldName.charAt(0)));
		String setMethodName = "set" + capFieldName;

		setValueMethod = ReflectionTools.getMethod(declaringClassDescriptor.getDescribedClass(),
				setMethodName, SET_METHOD_STRING_ARG);

		
		if (javaParser != null)
		{
			comment = javaParser.getJavaDocComment(field);
		}
		
		Type genericType = field.getGenericType();
		isGeneric = genericType instanceof ParameterizedType || genericType instanceof TypeVariable;
		if (genericType instanceof ParameterizedType)
		{
			// when it is parameterized, we need to take care of dependencies
			genericParametersString = XMLTools.getJavaGenericParametersString(field);
			ArrayList<Class> dependedClasses = XMLTools.getJavaGenericDependencies(field);
			if (dependedClasses != null)
			{
				for (Class dependedClass : dependedClasses)
				{
					addDependency(dependedClass);
				}
			}
		}
	}



	public String getUnresolvedScopeAnnotation()
	{
		return this.unresolvedScopeAnnotation;
	}

	protected void setUnresolvedScopeAnnotation(String scopeName)
	{
		this.unresolvedScopeAnnotation = scopeName;
	}
	
	public EnumerationDescriptor getEnumerationDescriptor()
	{
		return this.enumDescriptor;
	}

	/**
	 * Process annotations for polymorphic fields. These use meta-language to map tags for translate
	 * from based on classes (instead of field names).
	 * 
	 * @param field
	 * @return
	 */
	private boolean derivePolymorphicDescriptors(Field field)
	{
		// @xml_scope
		final simpl_scope scopeAnnotationObj = field.getAnnotation(simpl_scope.class);
		final String scopeAnnotation = (scopeAnnotationObj == null) ? null : scopeAnnotationObj.value();

		if (scopeAnnotation != null && scopeAnnotation.length() > 0)
		{
			if (!resolveScopeAnnotation(scopeAnnotation))
			{
				unresolvedScopeAnnotation = scopeAnnotation;
				declaringClassDescriptor.registerUnresolvedScopeAnnotationFD(this);
			}
		}
		
		
		// @xml_classes
		final simpl_classes classesAnnotationObj = field.getAnnotation(simpl_classes.class);
		final Class[] classesAnnotation = (classesAnnotationObj == null) ? null : classesAnnotationObj
				.value();
		if ((classesAnnotation != null) && (classesAnnotation.length > 0))
		{
			unresolvedClassesAnnotation = classesAnnotation;
			declaringClassDescriptor.registerUnresolvedClassesAnnotationFD(this);
		}
		return polymorphClassDescriptors != null;
	}

	/**
	 * Register a ClassDescriptor that is polymorphically engaged with this field.
	 * 
	 * @param classDescriptor
	 */
	protected void registerPolymorphicDescriptor(ClassDescriptor classDescriptor)
	{
		if (polymorphClassDescriptors == null)
			initPolymorphClassDescriptorsArrayList(1);

		String classTag = classDescriptor.getTagName();
		polymorphClassDescriptors.put(classTag, classDescriptor);
		tlvClassDescriptors.put(classTag.hashCode(), classDescriptor);

		ArrayList<String> otherTags = classDescriptor.otherTags();
		if (otherTags != null)
			for (String otherTag : otherTags)
			{
				if ((otherTag != null) && (otherTag.length() > 0))
				{
					polymorphClassDescriptors.put(otherTag, classDescriptor);
					tlvClassDescriptors.put(otherTag.hashCode(), classDescriptor);
				}
			}
	}

	/**
	 * Generate tag -> class mappings for a @serial_scope declaration.
	 * 
	 * @param scopeAnnotation
	 *          Name of the scope to lookup in the global space. Must be non-null.
	 * 
	 * @return true if the scope annotation is successfully resolved to a TranslationScope.
	 */
	private boolean resolveScopeAnnotation(final String scopeAnnotation)
	{
		SimplTypesScope scope =null; // TODO: MANAGE STS GET //SimplTypesScope.get(scopeAnnotation);
		if (scope != null)
		{
		
			Collection<ClassDescriptor<? extends FieldDescriptor>> scopeClassDescriptors = null; // TODO: getClassDescriptors scope.getClassDescriptors();
			initPolymorphClassDescriptorsArrayList(scopeClassDescriptors.size());
			for (ClassDescriptor<? extends FieldDescriptor> classDescriptor : scopeClassDescriptors)
			{
				String tagName = classDescriptor.getTagName();
				polymorphClassDescriptors.put(tagName, classDescriptor);
				polymorphClasses.put(tagName, classDescriptor.getDescribedClass());
				tlvClassDescriptors.put(tagName.hashCode(), classDescriptor);
			}
		}
		else
		{

		}
		return scope != null;
	}

	/**
	 * Generate tag -> class mappings for a @serial_scope declaration.
	 * 
	 * @param scopeAnnotation
	 *          Name of the scope to lookup in the global space. Must be non-null.
	 * 
	 * @return true if the scope annotation is successfully resolved to a TranslationScope.
	 */
	private boolean resolveClassesAnnotation(Class[] classesAnnotation)
	{

		initPolymorphClassDescriptorsArrayList(classesAnnotation.length);
		for (Class thatClass : classesAnnotation)
		{
			ClassDescriptor classDescriptor = ClassDescriptor.getClassDescriptor(thatClass);
			registerPolymorphicDescriptor(classDescriptor);
			polymorphClasses.put(classDescriptor.getTagName(), classDescriptor.getDescribedClass());
		}
		return true;
	}

	/**
	 * If there is an unresolvedScopeAnnotation, because a scope had not yet been declared when a
	 * ClassDescriptor that uses it was constructed, try again.
	 * 
	 * @return
	 */
	boolean resolveUnresolvedScopeAnnotation()
	{
		if (unresolvedScopeAnnotation == null)
			return true;

		boolean result = resolveScopeAnnotation(unresolvedScopeAnnotation);
		if (result)
		{
			unresolvedScopeAnnotation = null;
		}
		return result;
	}

	/**
	 * If there is an unresolvedScopeAnnotation, because a scope had not yet been declared when a
	 * ClassDescriptor that uses it was constructed, try again.
	 * 
	 * @return
	 */
	boolean resolveUnresolvedClassesAnnotation()
	{
		if (unresolvedClassesAnnotation == null)
		{
			return true;
		}

		boolean result = resolveClassesAnnotation(unresolvedClassesAnnotation);
		
		if (result)
		{
			unresolvedClassesAnnotation = null;
		}
		return result;
	}

	/**
	 * lazy-evaluation method.
	 * 
	 * @return
	 */
	public ArrayList<GenericTypeVar> getGenericTypeVars()
	{
		if (genericTypeVars == null)
		{
			synchronized (this)
			{
				if (genericTypeVars == null)
				{
					deriveGenericTypeVariables();
				}
			}
		}

		return genericTypeVars;
	}
	
	// added a setter to enable environment specific implementation -Fei
	public void setGenericTypeVars(ArrayList<GenericTypeVar> derivedGenericTypeVariables)
	{
		synchronized (this)
		{
			genericTypeVars = derivedGenericTypeVariables;
		}
	}

	public ArrayList<GenericTypeVar> getGenericTypeVarsContext()
	{
		return genericTypeVarsContextCD.getGenericTypeVars();
	}
	
	// This method is modified, refer to FundamentalPlatformSpecific package -Fei
	private void deriveGenericTypeVariables()
	{
		SimplPlatformSpecifics.get().deriveFieldGenericTypeVars(this);
	}

	private void initPolymorphClassDescriptorsArrayList(int initialSize)
	{
		if (polymorphClassDescriptors == null)
		{
			polymorphClassDescriptors = new HashMapArrayList<String, ClassDescriptor>(initialSize);
		}
			
		if (polymorphClasses == null)
		{
			polymorphClasses = new HashMap<String, Class>(initialSize);
		}
		
		if (tlvClassDescriptors == null)
		{
			tlvClassDescriptors = new HashMap<Integer, ClassDescriptor>(initialSize);
		}
	}

	/**
	 * Bind the ScalarType for a scalar typed field (attribute, leaf node, text). As appropriate,
	 * derive other context for scalar fields (is leaf, format).
	 * <p/>
	 * This method should only be called when you already know the field has a scalar annotation.
	 * 
	 * @param scalarField
	 *          Source for class & for annotations.
	 * 
	 * @return SCALAR, IGNORED_ATTRIBUTE< or IGNORED_ELEMENT
	 */
	private FieldType deriveScalarSerialization(Field scalarField)
	{
		FieldType result = deriveScalarSerialization(scalarField.getType(), scalarField);
		
		if (xmlHint == Hint.XML_TEXT || xmlHint == Hint.XML_TEXT_CDATA)
		{
			this.declaringClassDescriptor.setScalarTextFD(this);
		}
		
		return result;
	}

	/**
	 * Check for serialization hints for the field.
	 * 
	 * Lookup the scalar type for the class, and any serialization details, such as needsEscaping &
	 * format.
	 * 
	 * @param thatClass
	 *          Class that we seek a ScalarType for.
	 * @param field
	 *          Field to acquire annotations about the serialization.
	 * 
	 * @return SCALAR, IGNORED_ATTRIBUTE< or IGNORED_ELEMENT
	 */
	private FieldType deriveScalarSerialization(Class thatClass, Field field)
	{
		isEnum = FieldCategorizer.isEnum(field);
		xmlHint = XMLTools.simplHint(field); // TODO -- confirm that default case is acceptable
		scalarType = TypeRegistry.getScalarType(thatClass);

		this.fieldTypeSimpleName = field.getType().getSimpleName();
		
		if(this.isEnum)
		{
			try{
			this.enumDescriptor = EnumerationDescriptor.get(thatClass);
			}
			catch(SIMPLDescriptionException sde)
			{
				throw new RuntimeException(sde);
			}
		}
		
		if (isEnum == false && scalarType == null)
		{
			String msg = "Can't find ScalarType to serialize field: \t\t" + thatClass.getSimpleName()
					+ "\t" + field.getName() + ";";
			//warning("In class " + declaringClassDescriptor.getDescribedClass().getName(), msg);
			return (xmlHint == Hint.XML_ATTRIBUTE) ? FieldType.IGNORED_ATTRIBUTE : FieldType.IGNORED_ELEMENT;
		}

		format = XMLTools.getFormatAnnotation(field);
		
		if (xmlHint != Hint.XML_ATTRIBUTE)
		{
			isCDATA = xmlHint == Hint.XML_LEAF_CDATA || xmlHint == Hint.XML_TEXT_CDATA;
		}

		simpl_filter filterAnnotation = field.getAnnotation(simpl_filter.class);
		if (filterAnnotation != null)
		{
			String regexString = filterAnnotation.regex();
			if (regexString != null && regexString.length() > 0)
			{
				filterRegex = Pattern.compile(regexString);
				filterGroup = filterAnnotation.group();
				filterReplace = filterAnnotation.replace();
			}
		}
		return FieldType.SCALAR;
	}

	/**
	 * Figure out the type of field. Build associated data structures, such as collection or element
	 * class & tag. Process @simpl_other_tags.
	 * 
	 * @param field
	 * @param annotationType
	 *          Partial type information from the field declaration annotations, which are required.
	 */
	private FieldType deriveNestedSerialization(Field field, FieldType annotationType)
	{
		FieldType result = annotationType;
		Class fieldClass = field.getType();
		switch (annotationType)
		{
		case COMPOSITE_ELEMENT:

			String compositeTag = field.getAnnotation(simpl_composite.class).value();
			Boolean isWrap = field.isAnnotationPresent(simpl_wrap.class);

			boolean compositeTagIsNullOrEmpty = StringTools.isNullOrEmpty(compositeTag);
			if (!isPolymorphic())
			{
				if (isWrap && compositeTagIsNullOrEmpty)
				{
			//		warning("In " + declaringClassDescriptor.getDescribedClass()
				//			+ "\n\tCan't translate  @simpl_composite() " + field.getName()
					//		+ " because its tag argument is missing.");
					return FieldType.IGNORED_ELEMENT;
				}

				if (!isWrap & !compositeTagIsNullOrEmpty)
				{
				//	warning("In " + declaringClassDescriptor.getDescribedClass()
					//		+ "\n\tIgnoring argument to  @simpl_composite() " + field.getName()
						//	+ " because it is declared polymorphic.");
				}

				elementClassDescriptor = ClassDescriptor.getClassDescriptor(fieldClass);
				elementClass = elementClassDescriptor.getDescribedClass();
				compositeTag = XMLTools.getXmlTagName(field);
			}
			else
			{
				if (!compositeTagIsNullOrEmpty)
				{
				//	warning("In " + declaringClassDescriptor.getDescribedClass()
					//		+ "\n\tIgnoring argument to  @simpl_composite() " + field.getName()
						//	+ " because it is declared polymorphic.");
				}
			}
			compositeTagName = compositeTag;
			break;
		case COLLECTION_ELEMENT:
			final String collectionTag = field.getAnnotation(simpl_collection.class).value();
			if (!checkAssignableFrom(Collection.class, field, fieldClass, "@xml_collection"))
			{
				return FieldType.IGNORED_ELEMENT;
			}
			
			if (!isPolymorphic())
			{
				Class collectionElementClass = getTypeArgClass(field, 0); // 0th type arg for
				// Collection<FooState>

				if (collectionTag == null || collectionTag.isEmpty())
				{
				//	warning("In " + declaringClassDescriptor.getDescribedClass()
					//		+ "\n\tCan't translate  @xml_collection() " + field.getName()
						//	+ " because its tag argument is missing.");
					return FieldType.IGNORED_ELEMENT;
				}
				if (collectionElementClass == null)
				{
			//		warning("In " + declaringClassDescriptor.getDescribedClass()
				//			+ "\n\tCan't translate  @xml_collection() " + field.getName()
					//		+ " because the parameterized type argument for the Collection is missing.");
					return FieldType.IGNORED_ELEMENT;
				}
				if (!TypeRegistry.containsScalarTypeFor(collectionElementClass))
				{
					elementClassDescriptor = ClassDescriptor.getClassDescriptor(collectionElementClass);
					elementClass = elementClassDescriptor.getDescribedClass();
				}
				else
				{
					result = FieldType.COLLECTION_SCALAR;
					deriveScalarSerialization(collectionElementClass, field);
					// FIXME -- add error handling for IGNORED due to scalar type lookup fails
					if (scalarType == null)
					{
						result = FieldType.IGNORED_ELEMENT;
						///warning("Can't identify ScalarType for serialization of " + collectionElementClass);
					}
				}
			}
			else
			{
				// If Polymorphic... 
				if (collectionTag != null && !collectionTag.isEmpty())
				{
//					warning("In " + declaringClassDescriptor.getDescribedClass()
	//						+ "\n\tIgnoring argument to  @xml_collection() " + field.getName()
		//					+ " because it is declared polymorphic with @xml_classes.");
				}
			}

			collectionOrMapTagName = collectionTag;

			break;
		case MAP_ELEMENT:
			String mapTag = field.getAnnotation(simpl_map.class).value();
			if (!checkAssignableFrom(Map.class, field, fieldClass, "@xml_map"))
			{
				return FieldType.IGNORED_ELEMENT;
			}
			
			if (!isPolymorphic())
			{
				Class mapElementClass = getTypeArgClass(field, 1); // "1st" type arg for Map<FooState>

				if (mapTag == null || mapTag.isEmpty())
				{
			//		warning("In " + declaringClassDescriptor.getDescribedClass()
				//			+ "\n\tCan't translate  @xml_map() " + field.getName()
					//		+ " because its tag argument is missing.");
					return FieldType.IGNORED_ELEMENT;
				}
				if (mapElementClass == null)
				{
		//			warning("In " + declaringClassDescriptor.getDescribedClass()
			//				+ "\n\tCan't translate  @xml_map() " + field.getName()
				//			+ " because the parameterized type argument for the Collection is missing.");
					return FieldType.IGNORED_ELEMENT;
				}

				elementClassDescriptor = ClassDescriptor.getClassDescriptor(mapElementClass);
				elementClass = elementClassDescriptor.getDescribedClass();
				// }
				// else
				// {
				// result = MAP_SCALAR; // TODO -- do we really support this case??
				// // FIXME -- add error handling for IGNORED due to scalar type lookup fails
				// deriveScalarSerialization(mapElementClass, field);
				// }
			}
			else
			{
				if (mapTag != null && !mapTag.isEmpty())
				{
		////			warning("In " + declaringClassDescriptor.getDescribedClass()
			///				+ "\n\tIgnoring argument to  @xml_map() " + field.getName()
				//			+ " because it is declared polymorphic with @xml_classes.");
				}
			}
			collectionOrMapTagName = mapTag;

			break;
		default:
			break;
		}
		
		switch (annotationType)
		// set-up wrap as appropriate
		{
		case COLLECTION_ELEMENT:
		case MAP_ELEMENT:
			if (!field.isAnnotationPresent(simpl_nowrap.class))
			{
				wrapped = true;
			}
			

			break;
		case COMPOSITE_ELEMENT:
			if (field.isAnnotationPresent(simpl_wrap.class))
			{
				wrapped = true;
			}
			break;
		}

		if (result == FieldType.UNSET_TYPE)
		{
		//	warning("Programmer error -- can't derive type.");
			result = FieldType.IGNORED_ELEMENT;
		}

		return result;
	}


	private boolean checkAssignableFrom(Class targetClass, Field field, Class fieldClass,
			String annotationDescription)
	{
		boolean result = targetClass.isAssignableFrom(fieldClass);
		if (!result)
		{
	//		warning("In " + declaringClassDescriptor.getDescribedClass() + "\n\tCan't translate  "
		//			+ annotationDescription + "() " + field.getName()
			//		+ " because the annotated field is not an instance of " + targetClass.getSimpleName()
				//	+ ".");
		}
		return result;
	}

	/**
	 * Get the value of the ith declared type argument from a field declaration. Only works when the
	 * type variable is directly instantiated in the declaration.
	 * <p/>
	 * DOES NOT WORK when the type variable is instantiated outside the declaration, and passed in.
	 * This is because in Java, generic type variables are (lamely!) erased after compile time. They
	 * do not exist at runtime :-( :-( :-(
	 * 
	 * @param field
	 * @param i
	 *          Index of the type variable in the field declaration.
	 * 
	 * @return The class of the type variable, if it exists.
	 */
	
	// This method is modified to enable platform specific implementation
	public Class<?> getTypeArgClass(Field field, int i)
	{
		return SimplPlatformSpecifics.get().getTypeArgClass(field, i, this);
	}

	/**
	 * 
	 * @return true if this field represents a ScalarType, not a nested element or collection thereof.
	 */
	public boolean isScalar()
	{
		return scalarType != null;
	}

	public boolean isCollection()
	{
		FieldType ft = type;
		switch (ft)
		{
			case MAP_ELEMENT:
			case MAP_SCALAR:
			case COLLECTION_ELEMENT:
			case COLLECTION_SCALAR:
				return true;
			default:
				return false;
		}
	}

	public boolean isNested()
	{
		return this.getType() == FieldType.COMPOSITE_ELEMENT;
	}

	public boolean isEnum()
	{
		return isEnum;
	}

	public Hint getXmlHint()
	{
		return xmlHint;
	}

	/**
	 * @return the scalarType of the field
	 */
	public ScalarType getScalarType()
	{
		return scalarType;
	}

	/**
	 * @return the field
	 */
	public Field getField()
	{
		return field;
	}

	/**
	 * @return the class of the field
	 */
	public Class<?> getFieldType()
	{
		return field.getType();
	}

	/**
	 * 
	 * @return The OptimizationTypes type of the field.
	 */
	public FieldType getType()
	{
		return type;
	}
	
	public void setType(FieldType ft)
	{
		this.type = ft;
	}

	public Object getNested(Object context)
	{
		return ReflectionTools.getFieldValue(context, field);
	}

	public Map getMap(Object context)
	{
		return (Map) ReflectionTools.getFieldValue(context, field);
	}

	public Collection getCollection(Object context)
	{
		return (Collection) ReflectionTools.getFieldValue(context, field);
	}

	public boolean isMixin()
	{
		return false;
	}

	public Object getAndPerhapsCreateNested(Object context)
	{
		Object result = getNested(context);

		if (result == null)
		{
			result = ReflectionTools.getInstance(field.getType());
			ReflectionTools.setFieldValue(context, field, result);
		}
		return result;
	}

	public boolean isWrapped()
	{
		return wrapped;
	}

	protected void setWrapped(boolean wrapped)
	{
		this.wrapped = wrapped;
	}
	public boolean isCDATA()
	{
		return isCDATA;
	}


	@Override
	public String toString()
	{
		String name = (field != null) ? field.getName() : "NO_FIELD";
		String clazz = declaringClassDescriptor == null ? "NO_CLASS" : declaringClassDescriptor.getDescribedClass().toString();
		String typeStr = type == null ? "NO_TYPE" : Integer.toHexString(type.getTypeID());
	// 	return this.getClassSimpleName() + "[" + name + " < " + clazz + " type=0x" + typeStr + "]";
		return "";// FIXME: 
	}

	/**
	 * If this field is polymorphic, a Collection of ClassDescriptors for the polymorphically
	 * associated classes.
	 * 
	 * @return Collection, or null, if the field is not polymorphic
	 */
	public Collection<ClassDescriptor> getPolymorphicClassDescriptors()
	{
		return (polymorphClassDescriptors == null || polymorphClassDescriptors.size() == 0) ? null
				: polymorphClassDescriptors.values();
	}

	/**
	 * If this field is polymorphic, a Collection of Strings of all possible tags for the
	 * polymorphically associated classes. This is usually the tagName field of each ClassDescriptor.
	 * But it may be more, specifically if any of the classes are defined with @simpl_other_tags.
	 * 
	 * @return Collection, or null, if the field is not polymorphic
	 */
	public Collection<String> getPolymorphicTags()
	{
		return (polymorphClassDescriptors == null || polymorphClassDescriptors.size() == 0) ? null
				: polymorphClassDescriptors.keySet();
	}

	public HashMap<String, Class> getPolymorphicClasses()
	{
		if (polymorphClasses == null)
		{
			return null;
		}
		else
		{
			return polymorphClasses;
		}
	}

	
	

	// ----------------------------- methods from TagDescriptor
	// ---------------------------------------//
	/**
	 * Use a set method or the type system to set our field in the context to the value.
	 * 
	 * @param context
	 * @param value
	 * @param scalarUnmarshallingContext
	 *          TODO
	 */
	public void setFieldToScalar(Object context, String value,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		if(value != null)
		{
			value = filterValue(value);
		}
		
		if (!isCDATA && value != null)
		{
			value = XMLTools.unescapeXML(value);
		}
		
		if(this.isEnum)
		{
			try{
				Object unmarshalledEnum = this.getEnumerationDescriptor().unmarshal(value);

				// TODO FIX ME. :) 
			
			}
			catch(Exception e)
			{
				// We need better error handling here, obvi. but this should bubble up an exception 
				// to current error handling code. 
				throw new RuntimeException(e);
			}
		
		}
		else
		{
			if (setValueMethod != null)
			{
				// if the method is found, invoke the method
				// fill the String value with the value of the attr node
				// args is the array of objects containing arguments to the method to be invoked
				// in our case, methods have only one arg: the value String
				Object[] args = new Object[1];
				args[0] = value;
				try
				{
					setValueMethod.invoke(context, args); // run set method!
				}
				catch (InvocationTargetException e)
				{
				//	weird("couldnt run set method for " + tagName + " even though we found it");
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					//weird("couldnt run set method for " + tagName + " even though we found it");
					e.printStackTrace();
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
			}
			else if (scalarType != null)
			{
			//	scalarType.setField(context, field, value, format, scalarUnmarshallingContext);
			}
		}
	}

	public void setRegexFilter(Pattern regex, int group, String replacement)
	{
		filterRegex = regex;
		filterGroup = group;
		filterReplace = replacement;
	}

	/**
	 * Filter value using filterRegex.
	 * 
	 * @param value
	 * @return
	 */
	String filterValue(String value)
	{
		if (filterRegex != null)
		{
			Matcher matcher = filterRegex.matcher(value);
			if (filterReplace == null)
			{
				if (matcher.find())
				{
					value = matcher.group(filterGroup);
				}
				else
				{
					value = "";
				}
			}
			else
			{
				value = matcher.replaceAll(filterReplace);
			}
		}
		return value;
	}



	/**
	 * Generate an exception about problems accessing a field.
	 * 
	 * @param nestedElementState
	 * @param e
	 * @return
	 */
	private SIMPLTranslationException fieldAccessException(Object nestedElementState, Exception e)
	{
		return new SIMPLTranslationException("Unexpected Object / Field set problem. \n\t" + "Field = "
				+ field + "\n\ttrying to set to " + nestedElementState.getClass(), e);
	}

	

	public ClassDescriptor getChildClassDescriptor(String tagName)
	{
		resolveUnresolvedClassesAnnotation();
		resolveUnresolvedScopeAnnotation();
		ClassDescriptor childClassDescriptor = !isPolymorphic() ? elementClassDescriptor
				: polymorphClassDescriptors.get(tagName);

		return childClassDescriptor;
	}

	Object constructChildElementState(ElementState parent, String tagName)
			throws SIMPLTranslationException
	{
		ClassDescriptor childClassDescriptor = !isPolymorphic() ? elementClassDescriptor
				: polymorphClassDescriptors.get(tagName);
		Object result = null;
		if (childClassDescriptor != null)
		{
			result = getInstance(childClassDescriptor);

			// if (result != null)
			// result.setupInParent(parent, childClassDescriptor);
		}
		return result;
	}

	private Object getInstance(ClassDescriptor childClassDescriptor) throws SIMPLTranslationException
	{

		return childClassDescriptor.getInstance();

	}

	public void setFieldToComposite(Object context, Object nestedObject)
			throws SIMPLTranslationException
	{
		try
		{
			field.set(context, nestedObject);
		}
		catch (Exception e)
		{
			throw fieldAccessException(nestedObject, e);
		}
	}

	// ----------------------------- constant instances ---------------------------------------//
	public static FieldDescriptor makeIgnoredFieldDescriptor(String tag)
	{
		return new FieldDescriptor(tag);
	}

	FieldDescriptor(String tag)
	{
		this.tagName = tag;
		this.type = FieldType.IGNORED_ELEMENT;
		this.field = null;
		this.declaringClassDescriptor = null;
	}

	public static final FieldDescriptor	IGNORED_ELEMENT_FIELD_DESCRIPTOR;

	static
	{
		IGNORED_ELEMENT_FIELD_DESCRIPTOR = new FieldDescriptor("IGNORED");
	}

	// ----------------------------- convenience methods ---------------------------------------//

	public String elementStart()
	{
		return isCollection() ? collectionOrMapTagName : isNested() ? compositeTagName : tagName;
	}

	/**
	 * Most fields derive their tag from Field name for marshaling. However, some, such as those
	 * annotated with @xml_class, @xml_classes, @xml_scope, derive their tag from the class of an
	 * instance. This includes all polymorphic fields.
	 * 
	 * @return true if the tag name name is derived from the class name ( not the usual case, but
	 *         needed for polymorphism).
	 * 
	 *         else if the tag name is derived from the class name for @xml_nested or, for @xml_collection
	 *         and @xml_map), the tag name is derived from the annotation's value
	 */
	public boolean isPolymorphic()
	{
		return (polymorphClassDescriptors != null) || (unresolvedScopeAnnotation != null)
				|| (unresolvedClassesAnnotation != null);
		// else return true;
		// return tagClassDescriptors != null;
	}

	public String getCollectionOrMapTagName()
	{
		return collectionOrMapTagName;
	}

	protected void setCollectionOrMapTagName(String collectionOrMapTagName)
	{
		this.collectionOrMapTagName = collectionOrMapTagName;
	}

	// FIXME -- these are temporary bullshit declarations which need to be turned into something real
	public boolean hasXmlText()
	{
		return false;
	}

	public boolean isXmlNsDecl()
	{
		return false;
	}

	public FieldDescriptor getWrappedFD()
	{
		return wrappedFD;
	}
	
	protected void setWrappedFD(FieldDescriptor wrappedFD)
	{
		this.wrappedFD = wrappedFD;
		wrappedFD.wrapper = this;
	}

	public boolean belongsTo(ClassDescriptor c)
	{
		// FIXME here should we use ClassDescriptor instead of Class? this is used by java code gen.
		return (this.getDeclaringClassDescriptor() == c);
		// return this.getDeclaringClassDescriptor().getDescribedClass() == c.getDescribedClass();
	}

	@Override
	public ArrayList<String> otherTags()
	{
		ArrayList<String> result = this.otherTags;
		if (result == null)
		{
			result = new ArrayList<String>();
			if (this.getField() != null)
			{
				final simpl_other_tags otherTagsAnnotation = this.getField().getAnnotation(
						simpl_other_tags.class);

				// commented out since getAnnotation also includes inherited annotations
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

	public ClassDescriptor getDeclaringClassDescriptor()
	{
		return declaringClassDescriptor;
	}

	public ClassDescriptor getElementClassDescriptor()
	{
		return elementClassDescriptor;
	}

/**
	 * @return the name of the field used for key in this map. this is indicated through {@code
	 *         @simpl_map_key_field}, and is de/serializable.
	 */
	public String getMapKeyFieldName()
	{
		return this.mapKeyFieldName;
	}
	
	public Object getMapKeyFieldValue(Object mapElement)
	{
		if (this.mapKeyFieldName != null)
		{
			ClassDescriptor cd = ClassDescriptor.getClassDescriptor(mapElement);
			if (cd != null)
			{
				// TODO FIXME
				//FieldDescriptor fd = cd.getFieldDescriptorByFieldName(mapKeyFieldName);
				//return fd.getValue(mapElement);
			}
		}
		return null;
	}
	
	public void setElementClassDescriptor(ClassDescriptor elementClassDescriptor)
	{
		this.elementClassDescriptor = elementClassDescriptor;
		Class elementClass = elementClassDescriptor.getDescribedClass();
		if (elementClass != null)
			this.elementClass = elementClass;
	}

	public ClassDescriptor elementClassDescriptor(String tagName)
	{
	  if (isPolymorphic())
	  {
	    if (polymorphClassDescriptors == null)
	      derivePolymorphicDescriptors(field);
	    if (polymorphClassDescriptors != null)
	      return polymorphClassDescriptors.get(tagName);
	  }
    return elementClassDescriptor;
	}

	public ClassDescriptor elementClassDescriptor(int tlvId)
	{
		return (!isPolymorphic()) ? elementClassDescriptor : tlvClassDescriptors.get(tlvId);
	}
	
	@Override
	public String key()
	{
		return this.name;
	}

	public boolean IsGeneric()
	{
		return isGeneric;
	}

	public String getGenericParametersString()
	{
		return genericParametersString;
	}
	
	public ArrayList<ClassDescriptor> getDependencies()
	{
		return dependencies;
	}

	public void addDependency(ClassDescriptor dependedClassD)
	{
		if (dependencies == null)
			dependencies = new ArrayList<ClassDescriptor>();
		dependencies.add(dependedClassD);
	}

	public void addDependency(Class dependedClass)
	{
		// for those classes not SIMPL-enabled, this creates a surrogate class descriptor.
		addDependency(new ClassDescriptor(dependedClass));
	}
	
	public Object getValue(Object context)
	{
		Object resultObject = null;
		Field childField = this.getField();
		try
		{
			resultObject = childField.get(context);
		}
		catch (IllegalAccessException e)
		{
		//	debugA("WARNING re-trying access! " + e.getStackTrace()[0]);
			childField.setAccessible(true);
			try
			{
				resultObject = childField.get(this);
			}
			catch (IllegalAccessException e1)
			{
				//error("Can't access " + childField.getName());
				e1.printStackTrace();
			}
		}
		return resultObject;
	}

	public boolean isCollectionTag(String tagName)
	{
		resolveUnresolvedClassesAnnotation();
		resolveUnresolvedScopeAnnotation();
		return isPolymorphic() ? polymorphClassDescriptors.containsKey(tagName)
				: collectionOrMapTagName.equals(tagName);
	}

	public FieldDescriptor getDescriptorClonedFrom()
	{
		return clonedFrom;
	}

	/**
	 * @return The list of meta-information (annotations, attributes, etc.) for this field.
	 */
	public List<MetaInformation> getMetaInformation()
	{
		if (this.metaInfo == null)
		{
			this.metaInfo = new ArrayList<MetaInformation>();

			FieldType type = this.getType();
			String collectionMapTagValue = getCollectionOrMapTagName();

			if (type == FieldType.COMPOSITE_ELEMENT)
			{
				// @simpl_composite
				metaInfo.add(new MetaInformation(simpl_composite.class));

				// @simpl_wrap
				if (isWrapped())
					metaInfo.add(new MetaInformation(simpl_wrap.class));
			}
			else if (type == FieldType.COLLECTION_ELEMENT || type == FieldType.COLLECTION_SCALAR)
			{
				addDependency(List.class);

				// @simpl_collection
				if (isPolymorphic())
					metaInfo.add(new MetaInformation(simpl_collection.class));
				else
					metaInfo.add(new MetaInformation(simpl_collection.class, false, collectionMapTagValue));

				// @simpl_nowrap
				if (!isWrapped())
				{
					metaInfo.add(new MetaInformation(simpl_nowrap.class));
				}
			}
			else if (type == FieldType.MAP_ELEMENT)
			{
				// @simpl_map
				metaInfo.add(new MetaInformation(simpl_map.class, false, collectionMapTagValue));
			}
			else
			{
				// @simpl_scalar
				metaInfo.add(new MetaInformation(simpl_scalar.class));

				// @simpl_hints
				Hint hint = getXmlHint();
				if (hint != null)
				{
					addDependency(Hint.class);
					metaInfo.add(new MetaInformation(simpl_hints.class, true, hint));
				}
				
				// @simpl_filter
				if (filterRegex != null && filterRegex.pattern().length() > 0)
				{
					List<String> argNames = new ArrayList<String>();
					List<Object> argValues = new ArrayList<Object>();
					argNames.add("regex");
					argValues.add(filterRegex.pattern());
					if (filterGroup > 0)
					{
						argNames.add("group");
						argValues.add(filterGroup);
					}
					if (filterReplace != null)
					{
						argNames.add("replace");
						argValues.add(filterReplace);
					}
					metaInfo.add(new MetaInformation(simpl_filter.class, argNames.toArray(new String[] {}),
							argValues.toArray()));
				}
			}

			// @simpl_tag
			String autoTagName = XMLTools.getXmlTagName(getName(), null);
			if (tagName != null && !tagName.equals("") && !tagName.equals(autoTagName))
				metaInfo.add(new MetaInformation(simpl_tag.class, false, tagName));

			// @simpl_other_tags
			ArrayList<String> otherTags = otherTags();
			if (otherTags != null && otherTags.size() > 0)
				metaInfo.add(new MetaInformation(simpl_other_tags.class, true, otherTags.toArray()));

			// @simpl_classes
			Collection<ClassDescriptor> polyClassDescriptors = getPolymorphicClassDescriptors();
			if (polyClassDescriptors != null)
			{
				List<Argument> args = new ArrayList<Argument>();
				for (ClassDescriptor polyClassD : polyClassDescriptors)
				{
					Argument a = new Argument();
					a.value = polyClassD;
					a.typeName = polyClassD.getDescribedClassName();
					a.simpleTypeName = polyClassD.getDescribedClassSimpleName();
					args.add(a);
					addDependency(polyClassD);
				}
				MetaInformation simplClasses = new MetaInformation(simpl_classes.class, true, args);
				metaInfo.add(simplClasses);
			}

			// @simpl_scope
			String polyScope = getUnresolvedScopeAnnotation();
			if (polyScope != null && polyScope.length() > 0)
				metaInfo.add(new MetaInformation(simpl_scope.class, false, polyScope));
		}
		return metaInfo;
	}

	public String getFieldTypeSimpleName()
	{
		return this.fieldTypeSimpleName;
	}
	
	public FieldDescriptor getWrapper()
	{
		return wrapper;
	}

	protected void setWrapper(FieldDescriptor wrapper)
	{
		this.wrapper = wrapper;
	}
	
	protected String getFieldTypeName()
	{
		return fieldType;
	}
	
}
