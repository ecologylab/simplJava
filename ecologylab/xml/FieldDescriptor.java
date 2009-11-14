/**
 * 
 */
package ecologylab.xml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import ecologylab.generic.Debug;
import ecologylab.generic.ReflectionTools;
import ecologylab.xml.types.scalar.ScalarType;
import ecologylab.xml.types.scalar.TypeRegistry;

/**
 * Used to provide convenient access for setting and getting values, using the ecologylab.xml type system.
 * Provides marshalling and unmarshalling from Strings.
 * 
 * @author andruid
 */
public class FieldDescriptor extends Debug
implements ClassTypes
{
	public static final String	NULL	= ScalarType.DEFAULT_VALUE_STRING;
	
	protected final 	Field					field;
	final	String										tagName;
	
	/**
	 * Descriptor for the class that this field is declared in.
	 */
	protected final ClassDescriptor	declaringClassDescriptor;

	ScalarType<?>										scalarType;
	final  int											type;
  
  private boolean									isCDATA;
  
  private boolean									needsEscaping;
  
  private String[]								format;

	/**
	 * For most fields, tag is derived from the field declaration (using field name or @xml_tag). 
	 * For these, this slot should be false.
	 * <p/>
	 * However, for some fields, such as those declared using @xml_class, @xml_classes, or @xml_scope,
	 * the tag is derived from the class declaration (using class name or @xml_tag).
	 * This is, for example, required for polymorphic nested and collection fields.
	 */
	boolean													deriveTagFromClass;	//TODO -- probably get rid of this, and replace with a method
	
	/**
	 * Null if the tag for this field is derived from its field declaration.
	 * For most fields, tag is derived from the field declaration (using field name or @xml_tag). 
	 * <p/>
	 * However, for some fields, such as those declared using @xml_class, @xml_classes, or @xml_scope,
	 * the tag is derived from the class declaration (using class name or @xml_tag).
	 * This is, for example, required for polymorphic nested and collection fields.
	 * For these fields, this slot contains an array of the legal classes, which will be bound to this field
	 * during translateFromXML().
	 */
	ArrayList<ClassDescriptor>								tagClassDescriptors;
	
	String collectionOrMapTagName;
	
  /**
   * true if this is a collection or map field annotated with the name of child elements.
   */
  private	boolean									hasCollectionOrMapTag;
  
  /**
   * Used for Collection and Map fields.
   * Tells if the XML should be wrapped by an intermediate element.
   */
  private boolean															wrapped;
	
	Method													setValueMethod;
	
	public static final Class[]	SET_METHOD_ARG	= {String.class};
	
	/**
	 * Field object for a Field within this, which is special, in that it should receive a scalar value.
	 */
	Field					xmlTextScalarField;
	
  /**
   * This field is used iff type is COLLECTION or MAP
   */
  private		Class		collectionOrMapElementClass;
  
	
	/**
	 * Constructor for the pseudo-FieldDescriptor associated with each ClassDesctiptor,
	 * for translateToXML of fields that deriveTagFromClass.
	 * @param baseClassDescriptor
	 */
	public FieldDescriptor(ClassDescriptor baseClassDescriptor)
	{
		this.declaringClassDescriptor				= baseClassDescriptor;
		this.field													= null;
		this.tagName												= baseClassDescriptor.getTagName();
		this.type														= PSEUDO_FIELD_DESCRIPTOR;
	}
/**
 * This is the normal constructor.
 * 
 * @param declaringClassDescriptor
 * @param field
 */
	public FieldDescriptor(ClassDescriptor declaringClassDescriptor, Field field /*, String nameSpacePrefix */)
  {
  	this.declaringClassDescriptor		= declaringClassDescriptor;
  	this.field											= field;
		field.setAccessible(true);

  	deriveTagFromClass							= setupTagFromClasses(field);
  	this.tagName										= deriveTagFromClass ? null : deriveTagFromFieldDeclaration(field);

  	//TODO XmlNs
//  	if (nameSpacePrefix != null)
//  	{
//  		tagName				= nameSpacePrefix + tagName;
//  	}
  	
  	//FIXME -- implement this next!!!
  	type														= deriveTypeFromField(field);
  	
  	setupScalarIfNeeded(field);

  	//FIXME -- implement this next!
//  	if (XMLTools.isNested(field))
//  		setupXmlText(ClassDescriptor.getClassDescriptor((Class<ElementState>) field.getType()));
  	
		handleXmlTextAnnotation();

		setValueMethod				= ReflectionTools.getMethod(field.getType(), "setValue", SET_METHOD_ARG);
  }

	private String deriveTagFromFieldDeclaration(Field field)
	{
		final ElementState.xml_collection collectionAnnotationObj	= field.getAnnotation(ElementState.xml_collection.class);
  	final String collectionAnnotation	= (collectionAnnotationObj == null) ? null : collectionAnnotationObj.value();
  	final ElementState.xml_map mapAnnotationObj	= field.getAnnotation(ElementState.xml_map.class);
  	final String mapAnnotation	= (mapAnnotationObj == null) ? null : mapAnnotationObj.value();
  	final ElementState.xml_tag tagAnnotationObj					= field.getAnnotation(ElementState.xml_tag.class);
  	final String tagAnnotation			= (tagAnnotationObj == null) ? null : tagAnnotationObj.value();
  	String tag;
  	if ((collectionAnnotation != null) && (collectionAnnotation.length() > 0))
  	{
  		tag											= collectionAnnotation;
  		hasCollectionOrMapTag	= true;
  	}
  	else if ((mapAnnotation != null) && (mapAnnotation.length() > 0))
  	{
  		tag											= mapAnnotation;
  		hasCollectionOrMapTag	= true;
  	}
  	else
  	{
  		tag											= XMLTools.getXmlTagName(field);
  	}
		return tag;
	}
  
	private boolean setupTagFromClasses(Field field)
	{
		final ElementState.xml_class classAnnotationObj		= field.getAnnotation(ElementState.xml_class.class);
  	final Class classAnnotation			= (classAnnotationObj == null) ? null : classAnnotationObj.value();
  	final ElementState.xml_classes classesAnnotationObj		= field.getAnnotation(ElementState.xml_classes.class);
  	final Class[] classesAnnotation			= (classesAnnotationObj == null) ? null : classesAnnotationObj.value();
  	final ElementState.xml_scope scopeAnnotationObj		= field.getAnnotation(ElementState.xml_scope.class);
  	final String scopeAnnotation			= (scopeAnnotationObj == null) ? null : scopeAnnotationObj.value();
  	
		if (scopeAnnotation != null && scopeAnnotation.length() > 0)
		{
			TranslationScope scope	= TranslationScope.get(scopeAnnotation);
			if (scope != null)
			{
				Collection<ClassDescriptor>	scopeClassDescriptors	= scope.getClassDescriptors();
				setupTagClassDescriptors(scopeClassDescriptors.size());
				for (ClassDescriptor classDescriptor : scopeClassDescriptors)
					tagClassDescriptors.add(classDescriptor);
			}
		}
		if ((classesAnnotation != null) && (classesAnnotation.length > 0))
  	{
			setupTagClassDescriptors(classesAnnotation.length);
			for (Class thatClass : classesAnnotation)
				if (ElementState.class.isAssignableFrom(thatClass))
					tagClassDescriptors.add(ClassDescriptor.getClassDescriptor(thatClass));
  	}
  	if (classAnnotation != null)
  	{
			setupTagClassDescriptors(1);
			tagClassDescriptors.add(ClassDescriptor.getClassDescriptor(classAnnotation));
  	}
  	return tagClassDescriptors != null;
	}

	private void assembleTagClassDescriptors(final Class[] classesAnnotation)
	{
		if ((classesAnnotation != null) && (classesAnnotation.length > 0))
  	{
			setupTagClassDescriptors(classesAnnotation.length);
			for (Class thatClass : classesAnnotation)
  		{
  			tagClassDescriptors.add(ClassDescriptor.getClassDescriptor(thatClass));
  		}
  	}
	}

	private void setupTagClassDescriptors(int initialSize)
	{
		if (tagClassDescriptors != null)
			tagClassDescriptors					= new ArrayList<ClassDescriptor>(initialSize);
	}
	
	private void setupScalarIfNeeded(Field field)
	{
		switch (type)
		{
			case LEAF_NODE_VALUE:
			case REGULAR_ATTRIBUTE:
			case COLLECTION_SCALAR:
			{
	  		scalarType		= TypeRegistry.getType(field);
	  		if (type == LEAF_NODE_VALUE)
	  		{
	  			isCDATA			= XMLTools.leafIsCDATA(field);
	  			needsEscaping	= scalarType.needsEscaping();
	  		}
	  		format			= XMLTools.getFormatAnnotation(field);
			}
		}
	}
	private void handleXmlTextAnnotation()
	{
		//FIXME -- use f2XO.xmlTextField instead ?
		xmlTextScalarField			= declaringClassDescriptor.getScalarTextField();
		/**
		 * can be null for mixins.
		 */
		if(xmlTextScalarField != null)
		{
			//println("debug");
			//FIXME -- use f2XO.xmlTextScalarType instead!
			FieldToXMLOptimizations xmlTextF2XO	= declaringClassDescriptor.fieldToXMLOptimizations(xmlTextScalarField, (String) null);
			/**
			 * The xmlTextF2XO has scalarType as null.
			 */
			//this.scalarType					= xmlTextF2XO.scalarType();
			this.scalarType 					= TypeRegistry.getType(xmlTextScalarField);

			//					fieldClass								= xmlTextScalarField.getType();
		}
	}




	/**
	 * Figure out the type.
	 * 
	 * @param field
	 */
	@SuppressWarnings("unchecked")
	private int deriveTypeFromField(Field field)
	{
		int	result			= UNSET_TYPE;
		Class fieldClass= field.getType();
		
  	final ElementState.xml_collection collectionAnnotationObj		= field.getAnnotation(ElementState.xml_collection.class);
  	final String collectionTag			= (collectionAnnotationObj == null) ? null : collectionAnnotationObj.value();
  	if (collectionAnnotationObj != null)
  	{
  		if (!deriveTagFromClass)
  		{
	    	Class collectionElementClass= getTypeArgClass(field, 0);	// 0th type arg for Collection<FooState>

	    	if (collectionTag == null)
	    	{
	  			warning("In " + declaringClassDescriptor.describedClass + 
	  					"\n\tCan't translate  @xml_collection() " + field.getName() + " because its tag argument is missing.");
	  			return IGNORED_ELEMENT;
	    	}
	    	if (collectionElementClass == null)
	    	{
	  			warning("In " + declaringClassDescriptor.describedClass + 
	  					"\n\tCan't translate  @xml_collection() " + field.getName() + " because the parameterized type argument for the Collection is missing.");
	  			return IGNORED_ELEMENT;
	    	}
	    	this.collectionOrMapElementClass	= collectionElementClass;
    		collectionOrMapTagName						= collectionTag;
    		
      	result												= ElementState.class.isAssignableFrom(fieldClass) ? COLLECTION_ELEMENT : COLLECTION_SCALAR;
  		}
  		else
  		{	// deriveTagFromClasses
  			//TODO Monday
  		}
    	// else
    	// is is element or scalar???
    	
    	return result;
  	}

  	ElementState.xml_map mapAnnotationObj	= field.getAnnotation(ElementState.xml_map.class);
  	String mapAnnotation				= (mapAnnotationObj == null) ? null : mapAnnotationObj.value();
  	if (mapAnnotationObj != null)
  	{	
   	}

  	return result;
	}	
	
	/**
	 * Get the value of the ith declared type argument from a field declaration.
	 * Only works when the type variable is directly instantiated in the declaration.
	 * <p/>
	 * DOES NOT WORK when the type variable is instantiated outside the declaration, and passed in.
	 * This is because in Java, generic type variables are (lamely!) erased after compile time.
	 * They do not exist at runtime :-( :-( :-(
	 * 
	 * @param field
	 * @param i		Index of the type variable in the field declaration.
	 * 
	 * @return		The class of the type variable, if it exists.
 */
	@SuppressWarnings("unchecked")
	public Class<?> getTypeArgClass(Field field, int i)
	{
		Class result	= null;
		
		java.lang.reflect.Type[] typeArgs	= ReflectionTools.getParameterizedTypeTokens(field);
		if (typeArgs != null)
		{
			final Type typeArg0					= typeArgs[i];
			if (typeArg0 instanceof Class)
			{
				result	= (Class) typeArg0;
			}
		}
		return result;
	}
	/**
	 * 
	 * @return	true if this field represents a ScalarType, not a nested element or collection thereof.
	 */
	public boolean isScalar()
	{
		return scalarType != null;
	}
	public boolean isCollection()
	{
		switch (type)
		{
		case MAP_ELEMENT:
		case COLLECTION_ELEMENT:
		case COLLECTION_SCALAR:
			return true;
		default:
			return false;
		}
	}
	
	public boolean isNested()
	{
		return type == REGULAR_NESTED_ELEMENT;
	}
	public boolean set(ElementState context, String valueString)
	{
		return set(context, valueString, null);
	}
	/**
	 * In the supplied context object, set the *typed* value of the field,
	 * using the valueString passed in. 
	 * Unmarshalling is performed automatically, by the ScalarType already stored in this.
	 * <p/>
	 * Use a set method, if one is defined.
	 * 
	 * @param context			ElementState object to set the Field in this.
	 * 
	 * @param valueString		The value to set, which this method will use with the ScalarType, to create the value that will be set.
	 */
	//FIXME -- pass in ScalarUnmarshallingContext, and use it!
	public boolean set(ElementState context, String valueString, ScalarUnmarshallingContext scalarUnMarshallingContext)
	{
		boolean result	= false;
//		if ((valueString != null) && (context != null)) andruid & andrew 4/14/09 -- why not allow set to null?!
		if ((context != null))
		{
			if (xmlTextScalarField != null)	// this is for MetadataScalars, to set the value in the nested object, instead of operating directly on the value
			{
				try
				{
					ElementState nestedES	= (ElementState) field.get(context);
					if(nestedES == null)
					{
						//The field is not initialized...
						this.setField(context,field.getType().newInstance());
						nestedES	= (ElementState) field.get(context);
					}
					if (setValueMethod != null)
					{
						ReflectionTools.invoke(setValueMethod, nestedES, valueString);
						result			= true;
					}
					else 
						scalarType.setField(nestedES, xmlTextScalarField, valueString, null, scalarUnMarshallingContext);
					result			= true;

				} catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (InstantiationException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (isScalar())
			{
				scalarType.setField(context, field, valueString);
				result				= true;
			}
		}
		return result;
	}
	
	/**
	 * In the supplied context object, set the non-scalar field to a non-scalar value.
	 * 
	 * @param context
	 * 
	 * @param value		An ElementState, or a Collection, or a Map.
	 */
	public void set(ElementState context, Object value)
	{
		if (!isScalar())
		{
			setField(context, value);
		}
	}

	public void setField(ElementState context, Object value)
	{
		try
		{
			field.set(context, value);
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the String representation of the value of the field, in the context object, using the ScalarType.
	 * 
	 * @param context
	 * @return
	 */
	public String getValueString(ElementState context)
	{
		String	result	= NULL;
		if (context != null)
		{
			if (xmlTextScalarField != null)
			{
				try
				{
					ElementState nestedES	= (ElementState) field.get(context);

					//If nestedES is null...then the field is not initialized.
					if(nestedES != null)
					{
						result = scalarType.toString(xmlTextScalarField, nestedES);
					}
					
					
				} catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (isScalar())
			{
				result		= scalarType.toString(field, context);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @return	The Java name of the field.
	 */
	public String getFieldName()
	{
		return field.getName();
	}

	/**
	 * 
	 * @return	The XML tag name of the field.
	 */
	public String getTagName()
	{
		return tagName;
	}

	/**
	 * @return the scalarType of the field
	 */
	public ScalarType<?> getScalarType()
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
	 * @return	The OptimizationTypes type of the field.
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * @return the xmlTextScalarField
	 */
	public Field getXmlTextScalarField()
	{
		return xmlTextScalarField;
	}
	
	public ElementState getNested(ElementState context)
	{
		return (ElementState) ReflectionTools.getFieldValue(context, field);
	}
	
	public Map getMap(ElementState context)
	{
		return (Map) ReflectionTools.getFieldValue(context, field);
	}

	public Collection getCollection(ElementState context)
	{
		return (Collection) ReflectionTools.getFieldValue(context, field);
	}
	
	
	public boolean isPseudoScalar() 
	{
		return false;
	}

	public boolean isMixin() 
	{
		return false;
	}

	/**
	 * 
	 * @param context	Object that the field is in.
	 * 
	 * @return	true if the field is not a scalar or a psuedo-scalar, and it has a non null value.
	 */
	public boolean isNonNullReference(ElementState context)
	{
		boolean result	= false;
		try
		{
			result = (scalarType == null) && !isPseudoScalar() && (field.get(context) != null);
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public ElementState getAndPerhapsCreateNested(ElementState context)
	{
		ElementState result		= getNested(context);
		
		if (result == null)
		{
			result					= (ElementState) ReflectionTools.getInstance(field.getType());
			ReflectionTools.setFieldValue(context, field, result);
		}
		return result;
	}
	
	/**
	 * Most fields derive their tag from Field name for marshaling.
	 * However, some, such as those annotated with @xml_class, @xml_classes, @xml_scope,
	 * derive their tag from the class of an instance.
	 * This includes all polymorphic fields.
	 * 
	 * @return	true if tag is not derived from field name, but from the class of an instance.
	 */
	public boolean deriveTagFromClass()
	{
		return deriveTagFromClass;
	}
	public boolean isWrapped()
	{
		return wrapped;
	}


}

