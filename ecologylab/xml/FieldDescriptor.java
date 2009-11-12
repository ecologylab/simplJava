/**
 * 
 */
package ecologylab.xml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	
	protected final 	Field			field;
	final	String			tag;
	
	protected final ClassDescriptor	classDescriptor;

	ScalarType<?>			scalarType;
	final  int				type;
	
	Method						setValueMethod;
	
	public static final Class[]	SET_METHOD_ARG	= {String.class};
	
	/**
	 * Field object for a Field within this, which is special, in that it should receive a scalar value.
	 */
	Field					xmlTextScalarField;
	
	public FieldDescriptor(ClassDescriptor classDescriptor, FieldToXMLOptimizations f2XO)
	{
		this.classDescriptor	= classDescriptor;
		
		ScalarType scalarType = f2XO.scalarType();
		this.scalarType	= scalarType;

		this.field		= f2XO.field();
		field.setAccessible(true);
		this.tag	= f2XO.tagName();
		this.type			= f2XO.type();

		Class fieldClass 		= f2XO.getOperativeClass();

		//FIXME -- use f2XO.xmlTextField instead!
		xmlTextScalarField			= classDescriptor.getScalarTextField();
		/**
		 * can be null for mixins.
		 */
		if(xmlTextScalarField != null)
		{
			//println("debug");
			//FIXME -- use f2XO.xmlTextScalarType instead!
			FieldToXMLOptimizations xmlTextF2XO	= classDescriptor.fieldToXMLOptimizations(xmlTextScalarField, (String) null);
			/**
			 * The xmlTextF2XO has scalarType as null.
			 */
			//this.scalarType					= xmlTextF2XO.scalarType();
			this.scalarType 					= TypeRegistry.getType(xmlTextScalarField);

			//					fieldClass								= xmlTextScalarField.getType();
		}
		setValueMethod				= ReflectionTools.getMethod(fieldClass, "setValue", SET_METHOD_ARG);
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
		return tag;
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
}

