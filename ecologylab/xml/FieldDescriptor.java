/**
 * 
 */
package ecologylab.xml;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import ecologylab.generic.Debug;
import ecologylab.generic.ReflectionTools;
import ecologylab.xml.types.scalar.ScalarType;
import ecologylab.xml.types.scalar.TypeRegistry;

/**
 * Used to provide convenient access for setting and getting values, using the ecologylab.xml type
 * system. Provides marshalling and unmarshalling from Strings.
 * 
 * @author andruid
 */
public class FieldDescriptor extends ElementState implements ClassTypes
{
	public static final String			NULL						= ScalarType.DEFAULT_VALUE_STRING;

	protected final Field						field;

	private String										tagName;
	
	/**
	 * Used to specify old translations, for backwards compatability. Never written.
	 */
	@xml_collection("other_tag") private
	ArrayList<String>								otherTags;

	/**
	 * Descriptor for the class that this field is declared in.
	 */
	protected final ClassDescriptor	declaringClassDescriptor;

	private int												type;

	/**
	 * This slot makes sense only for attributes and leaf nodes
	 */
	private ScalarType<?>							scalarType;

	/**
	 * An option for scalar formatting.
	 */
	private String[]								format;

	private boolean									isCDATA;

	private boolean									needsEscaping;

	/**
	 * For most fields, tag is derived from the field declaration (using field name or @xml_tag). For
	 * these, this slot should be false.
	 * <p/>
	 * However, for some fields, such as those declared using @xml_class, @xml_classes, or @xml_scope,
	 * the tag is derived from the class declaration (using class name or @xml_tag). This is, for
	 * example, required for polymorphic nested and collection fields.
	 */
	boolean													deriveTagFromClass;																// TODO --
																																											// probably
																																											// get rid of
																																											// this, and
																																											// replace
																																											// with a
																																											// method

	/**
	 * Null if the tag for this field is derived from its field declaration. For most fields, tag is
	 * derived from the field declaration (using field name or @xml_tag).
	 * <p/>
	 * However, for some fields, such as those declared using @xml_class, @xml_classes, or @xml_scope,
	 * the tag is derived from the class declaration (using class name or @xml_tag). This is, for
	 * example, required for polymorphic nested and collection fields. For these fields, this slot
	 * contains an array of the legal classes, which will be bound to this field during
	 * translateFromXML().
	 */
	ArrayList<ClassDescriptor>			tagClassDescriptors;

	String													collectionOrMapTagName;

	/**
	 * true if this is a collection or map field annotated with the name of child elements.
	 */
	private boolean									hasCollectionOrMapTag;

	/**
	 * Used for Collection and Map fields. Tells if the XML should be wrapped by an intermediate
	 * element.
	 */
	private boolean									wrapped;
	

	Method													setValueMethod;

	public static final Class[]			SET_METHOD_ARG	=
																									{ String.class };

	/**
	 * Field object for a Field within this, which is special, in that it should receive a scalar
	 * value.
	 */
	Field														xmlTextScalarField;

	/**
	 * This field is used iff type is COLLECTION or MAP
	 */
	private Class										collectionOrMapElementClass;

	/**
	 * Constructor for the pseudo-FieldDescriptor associated with each ClassDesctiptor, for
	 * translateToXML of fields that deriveTagFromClass.
	 * 
	 * @param baseClassDescriptor
	 */
	public FieldDescriptor(ClassDescriptor baseClassDescriptor)
	{
		this.declaringClassDescriptor = baseClassDescriptor;
		this.field = null;
		this.tagName = baseClassDescriptor.getTagName();
		this.type = PSEUDO_FIELD_DESCRIPTOR;
		this.scalarType = null;
	}

	/**
	 * This is the normal constructor.
	 * 
	 * @param declaringClassDescriptor
	 * @param field
	 */
	public FieldDescriptor(ClassDescriptor declaringClassDescriptor, Field field) // String nameSpacePrefix
	{
		this.declaringClassDescriptor = declaringClassDescriptor;
		this.field = field;
		field.setAccessible(true);

		deriveTagFromClass = setupTagFromClasses(field);
		this.tagName = deriveTagFromClass ? null : deriveTagFromFieldDeclaration(field);

		// TODO XmlNs
		// if (nameSpacePrefix != null)
		// {
		// tagName = nameSpacePrefix + tagName;
		// }

		type = deriveTypeFromField(field);

		xmlTextScalarField = declaringClassDescriptor.getScalarTextField();

		if (xmlTextScalarField != null)
			scalarType = TypeRegistry.getType(xmlTextScalarField);
		else
			scalarType = setupScalarIfNeeded(field);

		// FIXME -- implement this next!
		// if (XMLTools.isNested(field))
		// setupXmlText(ClassDescriptor.getClassDescriptor((Class<ElementState>) field.getType()));

		setValueMethod = ReflectionTools.getMethod(field.getType(), "setValue", SET_METHOD_ARG);
	}

	private String deriveTagFromFieldDeclaration(Field field)
	{
		final ElementState.xml_collection collectionAnnotationObj = field
				.getAnnotation(ElementState.xml_collection.class);
		final String collectionAnnotation = (collectionAnnotationObj == null) ? null
				: collectionAnnotationObj.value();
		final ElementState.xml_map mapAnnotationObj = field.getAnnotation(ElementState.xml_map.class);
		final String mapAnnotation = (mapAnnotationObj == null) ? null : mapAnnotationObj.value();
		final ElementState.xml_tag tagAnnotationObj = field.getAnnotation(ElementState.xml_tag.class);
		final String tagAnnotation = (tagAnnotationObj == null) ? null : tagAnnotationObj.value();
		String tag;
		if ((collectionAnnotation != null) && (collectionAnnotation.length() > 0))
		{
			tag = collectionAnnotation;
			hasCollectionOrMapTag = true;
		}
		else if ((mapAnnotation != null) && (mapAnnotation.length() > 0))
		{
			tag = mapAnnotation;
			hasCollectionOrMapTag = true;
		}
		else
		{
			tag = XMLTools.getXmlTagName(field);
		}
		return tag;
	}

	private boolean setupTagFromClasses(Field field)
	{
		final ElementState.xml_class classAnnotationObj = field
				.getAnnotation(ElementState.xml_class.class);
		final Class classAnnotation = (classAnnotationObj == null) ? null : classAnnotationObj.value();
		final ElementState.xml_classes classesAnnotationObj = field
				.getAnnotation(ElementState.xml_classes.class);
		final Class[] classesAnnotation = (classesAnnotationObj == null) ? null : classesAnnotationObj
				.value();
		final ElementState.xml_scope scopeAnnotationObj = field
				.getAnnotation(ElementState.xml_scope.class);
		final String scopeAnnotation = (scopeAnnotationObj == null) ? null : scopeAnnotationObj.value();

		if (scopeAnnotation != null && scopeAnnotation.length() > 0)
		{
			TranslationScope scope = TranslationScope.get(scopeAnnotation);
			if (scope != null)
			{
				Collection<ClassDescriptor> scopeClassDescriptors = scope.getClassDescriptors();
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
			tagClassDescriptors = new ArrayList<ClassDescriptor>(initialSize);
	}

	private ScalarType setupScalarIfNeeded(Field field)
	{
		ScalarType result = null;
		switch (type)
		{
		case LEAF_NODE_VALUE:
		case REGULAR_ATTRIBUTE:
		case COLLECTION_SCALAR:
		{
			result = TypeRegistry.getType(field);
			if (type == LEAF_NODE_VALUE)
			{
				isCDATA = XMLTools.leafIsCDATA(field);
				needsEscaping = scalarType.needsEscaping();
			}
			format = XMLTools.getFormatAnnotation(field);
		}
		}
		return result;
	}

	/**
	 * Figure out the type.
	 * 
	 * @param field
	 */
	//FIXME -- not complete!!!! return to finish other cases!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	@SuppressWarnings("unchecked")
	private int deriveTypeFromField(Field field)
	{
		int result = UNSET_TYPE;
		Class fieldClass = field.getType();

		final ElementState.xml_collection collectionAnnotationObj = field
				.getAnnotation(ElementState.xml_collection.class);
		final String collectionTag = (collectionAnnotationObj == null) ? null : collectionAnnotationObj
				.value();
		if (collectionAnnotationObj != null)
		{
			if (Collection.class.isAssignableFrom(fieldClass))
			{
				warning("In " + declaringClassDescriptor.getDescribedClass()
						+ "\n\tCan't translate  @xml_collection() " + field.getName()
						+ " because the annotated field is not an instance of Collection.");
				return IGNORED_ELEMENT;
			}
			if (!deriveTagFromClass)
			{
				Class collectionElementClass = getTypeArgClass(field, 0); // 0th type arg for Collection<FooState>

				if (collectionTag == null)
				{
					warning("In " + declaringClassDescriptor.getDescribedClass()
							+ "\n\tCan't translate  @xml_collection() " + field.getName()
							+ " because its tag argument is missing.");
					return IGNORED_ELEMENT;
				}
				if (collectionElementClass == null)
				{
					warning("In " + declaringClassDescriptor.getDescribedClass()
							+ "\n\tCan't translate  @xml_collection() " + field.getName()
							+ " because the parameterized type argument for the Collection is missing.");
					return IGNORED_ELEMENT;
				}
				this.collectionOrMapElementClass = collectionElementClass;
				collectionOrMapTagName = collectionTag;

				result = ElementState.class.isAssignableFrom(fieldClass) ? COLLECTION_ELEMENT : COLLECTION_SCALAR;
				
				// other tags???
			}
			else
			{ // deriveTagFromClasses
				// TODO Monday
			}
			// else
			// is is element or scalar???
			
			ElementState.xml_other_tags otherTagsAnnotation 	= field.getAnnotation(ElementState.xml_other_tags.class);
			if (otherTagsAnnotation != null)
			{
				String[] otherTags	= XMLTools.otherTags(otherTagsAnnotation);
				if (otherTags.length > 0)
				{
					this.otherTags		= new ArrayList<String>(otherTags.length);
					for (String otherTag : otherTags)
					{
						if ((otherTag != null) && (otherTag.length() > 0))
						{
							this.otherTags.add(otherTag);
						}
					}
				}
			}
		}


			return result;
		}

		ElementState.xml_map mapAnnotationObj = field.getAnnotation(ElementState.xml_map.class);
		String mapAnnotation = (mapAnnotationObj == null) ? null : mapAnnotationObj.value();
		if (mapAnnotationObj != null)
		{
			Class mapElementClass = getTypeArgClass(field, 1); // "1st" type arg for Map<FooState>

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
	@SuppressWarnings("unchecked")
	public Class<?> getTypeArgClass(Field field, int i)
	{
		Class result = null;

		java.lang.reflect.Type[] typeArgs = ReflectionTools.getParameterizedTypeTokens(field);
		if (typeArgs != null)
		{
			final Type typeArg0 = typeArgs[i];
			if (typeArg0 instanceof Class)
			{
				result = (Class) typeArg0;
			}
		}
		return result;
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
	 * In the supplied context object, set the *typed* value of the field, using the valueString
	 * passed in. Unmarshalling is performed automatically, by the ScalarType already stored in this.
	 * <p/>
	 * Use a set method, if one is defined.
	 * 
	 * @param context
	 *          ElementState object to set the Field in this.
	 * 
	 * @param valueString
	 *          The value to set, which this method will use with the ScalarType, to create the value
	 *          that will be set.
	 */
	// FIXME -- pass in ScalarUnmarshallingContext, and use it!
	public boolean set(ElementState context, String valueString,
			ScalarUnmarshallingContext scalarUnMarshallingContext)
	{
		boolean result = false;
		// if ((valueString != null) && (context != null)) andruid & andrew 4/14/09 -- why not allow set
		// to null?!
		if ((context != null))
		{
			if (xmlTextScalarField != null) // this is for MetadataScalars, to set the value in the nested
																			// object, instead of operating directly on the value
			{
				try
				{
					ElementState nestedES = (ElementState) field.get(context);
					if (nestedES == null)
					{
						// The field is not initialized...
						this.setField(context, field.getType().newInstance());
						nestedES = (ElementState) field.get(context);
					}
					if (setValueMethod != null)
					{
						ReflectionTools.invoke(setValueMethod, nestedES, valueString);
						result = true;
					}
					else
						scalarType.setField(nestedES, xmlTextScalarField, valueString, null,
								scalarUnMarshallingContext);
					result = true;

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
				catch (InstantiationException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (isScalar())
			{
				scalarType.setField(context, field, valueString);
				result = true;
			}
		}
		return result;
	}

	/**
	 * In the supplied context object, set the non-scalar field to a non-scalar value.
	 * 
	 * @param context
	 * 
	 * @param value
	 *          An ElementState, or a Collection, or a Map.
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
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Get the String representation of the value of the field, in the context object, using the
	 * ScalarType.
	 * 
	 * @param context
	 * @return
	 */
	public String getValueString(ElementState context)
	{
		String result = NULL;
		if (context != null)
		{
			if (xmlTextScalarField != null)
			{
				try
				{
					ElementState nestedES = (ElementState) field.get(context);

					// If nestedES is null...then the field is not initialized.
					if (nestedES != null)
					{
						result = scalarType.toString(xmlTextScalarField, nestedES);
					}

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
			}
			else if (isScalar())
			{
				result = scalarType.toString(field, context);
			}
		}
		return result;
	}

	/**
	 * 
	 * @return The Java name of the field.
	 */
	public String getFieldName()
	{
		return field.getName();
	}

	/**
	 * 
	 * @return The XML tag name of the field.
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
	 * @return The OptimizationTypes type of the field.
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
	 * @param context
	 *          Object that the field is in.
	 * 
	 * @return true if the field is not a scalar or a psuedo-scalar, and it has a non null value.
	 */
	public boolean isNonNullReference(ElementState context)
	{
		boolean result = false;
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
		ElementState result = getNested(context);

		if (result == null)
		{
			result = (ElementState) ReflectionTools.getInstance(field.getType());
			ReflectionTools.setFieldValue(context, field, result);
		}
		return result;
	}

	/**
	 * Most fields derive their tag from Field name for marshaling. However, some, such as those
	 * annotated with @xml_class, @xml_classes, @xml_scope, derive their tag from the class of an
	 * instance. This includes all polymorphic fields.
	 * 
	 * @return true if tag is not derived from field name, but from the class of an instance.
	 */
	public boolean deriveTagFromClass()
	{
		return deriveTagFromClass;
	}

	public boolean isWrapped()
	{
		return wrapped;
	}

	/**
	 * Use this and the context to append an attribute / value pair to the StringBuilder passed in.
	 * 
	 * @param buffy
	 * @param context
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void appendValueAsAttribute(StringBuilder buffy, Object context)
			throws IllegalArgumentException, IllegalAccessException
	{
		if (context != null)
		{
			ScalarType scalarType = this.scalarType;
			Field field = this.field;

			if (scalarType == null)
			{
				weird("scalarType = null!");
			}
			else if (!scalarType.isDefaultValue(field, context))
			{
				// for this field, generate tags and attach name value pair

				// TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
				// (which is an instance variable of this) !!!

				buffy.append(' ');
				buffy.append(this.tagName);
				buffy.append('=');
				buffy.append('"');

				scalarType.appendValue(buffy, this, context);
				buffy.append('"');
			}
		}
	}

	/**
	 * Use this and the context to set an attribute (name, value) on the Element DOM Node passed in.
	 * 
	 * @param element
	 * @param context
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void setAttribute(Element element, Object context) throws IllegalArgumentException,
			IllegalAccessException
	{
		if (context != null)
		{
			ScalarType scalarType = this.scalarType;
			Field field = this.field;

			if (scalarType == null)
				weird("YO! setAttribute() scalarType == null!!!");
			else if (!scalarType.isDefaultValue(field, context))
			{
				// for this field, generate tags and attach name value pair

				// TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
				// (which is an instance variable of this) !!!

				String value = scalarType.toString(field, context);

				element.setAttribute(tagName, value);
			}
		}
	}

	/**
	 * Use this and the context to set an attribute (name, value) on the Element DOM Node passed in.
	 * 
	 * @param element
	 * @param instance
	 * @param isAtXMLText
	 *          TODO
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void appendLeaf(Element element, Object instance) throws IllegalArgumentException,
			IllegalAccessException
	{
		if (instance != null)
		{
			ScalarType scalarType = this.scalarType;

			Document document = element.getOwnerDocument();

			Object fieldInstance = field.get(instance);
			if (fieldInstance != null)
			{

				String fieldValueString = fieldInstance.toString();

				Text textNode = isCDATA ? document.createCDATASection(fieldValueString) : document
						.createTextNode(fieldValueString);

				Element leafNode = document.createElement(tagName);
				leafNode.appendChild(textNode);

				element.appendChild(leafNode);
			}
		}
	}

	public void appendXmlText(Element element, Object instance) throws IllegalArgumentException,
			IllegalAccessException
	{
		if (instance != null)
		{
			ScalarType scalarType = this.scalarType;
			if (!scalarType.isDefaultValue(xmlTextScalarField, instance))
			{
				Document document = element.getOwnerDocument();
	
				Object fieldInstance = xmlTextScalarField.get(instance);
				if (fieldInstance != null)
				{
					String fieldValueString = fieldInstance.toString();
	
					Text textNode = isCDATA ? document.createCDATASection(fieldValueString) : document
							.createTextNode(fieldValueString);
	
					element.appendChild(textNode);
				}
			}
		}
	}

	public void appendCollectionLeaf(Element element, Object instance)
			throws IllegalArgumentException, IllegalAccessException
	{
		if (instance != null)
		{
			Document document = element.getOwnerDocument();

			String instanceString = instance.toString();

			// Object fieldInstance = field.get(instance);
			// String fieldValueString= fieldInstance.toString();

			Text textNode = isCDATA ? document.createCDATASection(instanceString) : document
					.createTextNode(instanceString);

			Element leafNode = document.createElement(tagName);
			leafNode.appendChild(textNode);

			element.appendChild(leafNode);
		}
	}

	/**
	 * Use this and the context to append an attribute / value pair to the Appendable passed in.
	 * 
	 * @param appendable
	 * @param context
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public void appendValueAsAttribute(Appendable appendable, Object context)
			throws IllegalArgumentException, IllegalAccessException, IOException
	{
		if (context != null)
		{
			ScalarType scalarType = this.scalarType;
			Field field = this.field;

			if (!scalarType.isDefaultValue(field, context))
			{
				// for this field, generate tags and attach name value pair

				// TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
				// (which is an instance variable of this) !!!

				appendable.append(' ');
				appendable.append(tagName);
				appendable.append('=');
				appendable.append('"');

				scalarType.appendValue(appendable, this, context);
				appendable.append('"');
			}
		}
	}

	static final String	START_CDATA	= "<![CDATA[";

	static final String	END_CDATA		= "]]>";

	/**
	 * Use this and the context to append a leaf node with value to the StringBuilder passed in,
	 * unless it turns out that the value is the default.
	 * 
	 * @param buffy
	 * @param context
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	void appendLeaf(StringBuilder buffy, Object context) throws IllegalArgumentException,
			IllegalAccessException
	{
		if (context != null)
		{
			ScalarType scalarType = this.scalarType;
			Field field = this.field;
			if (!scalarType.isDefaultValue(field, context))
			{
				// for this field, generate <tag>value</tag>

				// TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
				// (which is an instance variable of this) !!!
				appendOpenTag(buffy);

				if (isCDATA)
					buffy.append(START_CDATA);
				scalarType.appendValue(buffy, this, context); // escape if not CDATA! :-)
				if (isCDATA)
					buffy.append(END_CDATA);

				appendCloseTag(buffy);
			}
		}
	}

	/**
	 * Use this and the context to append a text node value to the StringBuilder passed in, unless it
	 * turns out that the value is the default.
	 * 
	 * @param buffy
	 * @param context
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	void appendXmlText(StringBuilder buffy, Object context) throws IllegalArgumentException,
			IllegalAccessException
	{
		if (context != null)
		{
			ScalarType scalarType = this.scalarType;
			if (!scalarType.isDefaultValue(xmlTextScalarField, context))
			{
				// for this field, generate <tag>value</tag>

				if (isCDATA)
					buffy.append(START_CDATA);
				// TODO lkkljhkhj
				scalarType.appendValue(buffy, this, context); // escape if not CDATA! :-)
				if (isCDATA)
					buffy.append(END_CDATA);
			}
		}
	}

	void appendOpenTag(StringBuilder buffy)
	{
		buffy.append('<').append(tagName).append('>');
	}

	void appendCloseTag(StringBuilder buffy)
	{
		buffy.append('<').append('/').append(tagName).append('>');
	}

	void appendOpenTag(Appendable buffy) throws IOException
	{
		buffy.append('<').append(tagName).append('>');
	}

	void appendCloseTag(Appendable buffy) throws IOException
	{
		buffy.append('<').append('/').append(tagName).append('>');
	}

	/**
	 * Use this and the context to append a leaf node with value to the StringBuilder passed in.
	 * Consideration of default values is not evaluated.
	 * 
	 * @param buffy
	 * @param context
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	void appendCollectionLeaf(StringBuilder buffy, Object instance) throws IllegalArgumentException,
			IllegalAccessException
	{
		if (instance != null)
		{
			ScalarType scalarType = this.scalarType;

			appendOpenTag(buffy);
			if (isCDATA)
				buffy.append(START_CDATA);
			scalarType.appendValue(instance, buffy, !isCDATA); // escape if not CDATA! :-)
			if (isCDATA)
				buffy.append(END_CDATA);

			appendCloseTag(buffy);
		}
	}

	/**
	 * Use this and the context to append a leaf node with value to the Appendable passed in.
	 * Consideration of default values is not evaluated.
	 * 
	 * @param appendable
	 * @param context
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	void appendCollectionLeaf(Appendable appendable, Object instance)
			throws IllegalArgumentException, IllegalAccessException, IOException
	{
		if (instance != null)
		{
			ScalarType scalarType = this.scalarType;

			appendOpenTag(appendable);
			if (isCDATA)
				appendable.append(START_CDATA);
			scalarType.appendValue(instance, appendable, !isCDATA); // escape if not CDATA! :-)
			if (isCDATA)
				appendable.append(END_CDATA);

			appendCloseTag(appendable);
		}
	}

	/**
	 * Use this and the context to append a leaf node with value to the Appendable passed in.
	 * 
	 * @param buffy
	 * @param context
	 * @param isAtXMLText
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	void appendLeaf(Appendable appendable, Object context) throws IllegalArgumentException,
			IllegalAccessException, IOException
	{
		if (context != null)
		{
			ScalarType scalarType = this.scalarType;
			Field field = this.field;
			if (!scalarType.isDefaultValue(field, context))
			{
				// for this field, generate <tag>value</tag>

				// TODO if type.isFloatingPoint() -- deal with floatValuePrecision here!
				// (which is an instance variable of this) !!!

				appendOpenTag(appendable);

				if (isCDATA)
					appendable.append(START_CDATA);
				scalarType.appendValue(appendable, this, context); // escape if not CDATA! :-)
				if (isCDATA)
					appendable.append(END_CDATA);

				appendCloseTag(appendable);
			}
		}
	}

	void appendXmlText(Appendable appendable, Object context) throws IllegalArgumentException,
			IllegalAccessException, IOException
	{
		if (context != null)
		{
			ScalarType scalarType = this.scalarType;
			if (!scalarType.isDefaultValue(xmlTextScalarField, context))
			{
				// for this field, generate <tag>value</tag>

				if (isCDATA)
					appendable.append(START_CDATA);

				scalarType.appendValue(appendable, this, context); // escape if not CDATA! :-)
				if (isCDATA)
					appendable.append(END_CDATA);
			}
		}
	}

	public boolean isCDATA()
	{
		return isCDATA;
	}

	public boolean isNeedsEscaping()
	{
		return needsEscaping;
	}

	public String[] getFormat()
	{
		return format;
	}


}
