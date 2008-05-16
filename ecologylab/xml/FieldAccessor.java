/**
 * 
 */
package ecologylab.xml;

import java.lang.reflect.Field;
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
public class FieldAccessor extends Debug
{
	final 	Field			field;
	final	String			tagName;

	ScalarType<?>			scalarType;
	final  int				type;
	
	/**
	 * Field object for a Field within this, which is special, in that it should receive a scalar value.
	 */
	Field					xmlTextScalarField;
	
	public FieldAccessor(FieldToXMLOptimizations f2XO)
	{
		ScalarType scalarType = f2XO.scalarType();
		this.scalarType	= scalarType;
		
		this.field		= f2XO.field();
		this.tagName	= f2XO.tagName();
		this.type		= f2XO.type();

		Optimizations parentOptimizations	= f2XO.getContextOptimizations();
		if (parentOptimizations != null)
		{
			Class cl = f2XO.getOperativeClass();
			Optimizations thisOptimizations	= parentOptimizations.lookupChildOptimizations(f2XO.getOperativeClass());
			if (thisOptimizations != null)
			{
				//FIXME -- use f2XO.xmlTextField instead!
				xmlTextScalarField			= thisOptimizations.getScalarTextField();
				/**
				 * can be null for mixins.
				 */
				if(xmlTextScalarField != null)
				{
					//println("debug");
					//FIXME -- use f2XO.xmlTextScalarType instead!
					FieldToXMLOptimizations xmlTextF2XO	= parentOptimizations.fieldToXMLOptimizations(xmlTextScalarField, (String) null);
					/**
					 * The xmlTextF2XO has scalarType as null.
					 */
					scalarType					= xmlTextF2XO.scalarType();
					scalarType 					= TypeRegistry.getType(xmlTextScalarField);
					/**
					 * Not sure whether this is required.
					 */
				}			
			}
		}
	}
	
	/**
	 * 
	 * @return	true if this field represents a ScalarType, not a nested element or collection thereof.
	 */
	public boolean isScalar()
	{
		return scalarType != null;
	}
	
	/**
	 * In the supplied context object, set the *typed* value of the field,
	 * using the valueString passed in. 
	 * Unmarshalling is performed automatically, by the ScalarType already stored in this.
	 * 
	 * @param context			ElementState object to set the Field in this.
	 * 
	 * @param valueString		The value to set, which this method will use with the ScalarType, to create the value that will be set.
	 */
	public void set(ElementState context, String valueString)
	{
		if ((valueString != null) && (context != null))
		{
			if (xmlTextScalarField != null)
			{
				try
				{
					ElementState nestedES	= (ElementState) field.get(context);
					scalarType.setField(nestedES, xmlTextScalarField, valueString);
					
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
				scalarType.setField(context, field, valueString);
			}
		}
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

	//IncontextMetadata Transition
	public void setField(ElementState context, Object value)
	{
		try
		{
			field.set(context, value);
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
	
	/**
	 * Get the String representation of the value of the field, in the context object, using the ScalarType.
	 * 
	 * @param context
	 * @return
	 */
	public String getValueString(ElementState context)
	{
		String	result	= "null";
		if (context != null)
		{
			if (xmlTextScalarField != null)
			{
				try
				{
					ElementState nestedES	= (ElementState) field.get(context);
					if(nestedES == null)
					{
						println("debug");
						
					}
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
}

