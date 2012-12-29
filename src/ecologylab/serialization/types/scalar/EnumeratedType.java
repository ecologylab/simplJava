package ecologylab.serialization.types.scalar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_map;
import ecologylab.serialization.annotations.simpl_nowrap;

public class EnumeratedType extends ReferenceType<Enum>
{		
	@simpl_nowrap
	@simpl_collection("enum_string_constants")
	ArrayList<String> enumStringConstants = new ArrayList<String>();
	
	String enumTypeSimpleName = ""; 
	 
	public EnumeratedType()
	{
		super(Enum.class, null, null, null, null);
	}
	
	public EnumeratedType(Field field)
	{
		if (field.getType().isEnum())
		{
			Object[] enumArray = field.getType().getEnumConstants();
			for (Object enumObj : enumArray)
			{
				if (enumObj instanceof Enum<?>)
				{
					Enum<?> enumeratedType = ((Enum<?>) enumObj);
					enumStringConstants.add(enumeratedType.toString());
				}
			}
		}
		
		enumTypeSimpleName = field.getType().getSimpleName();
	}

	@Override
	public boolean setField(Object context, Field field, String valueString, String[] format, ScalarUnmarshallingContext scalarUnmarshallingContext)
   {
       if (valueString == null)
           return true;

       boolean result		= false;
       Enum<?> referenceObject;
       
       try
       {
      	 	referenceObject = XMLTools.createEnumeratedType(field, valueString);
          if (referenceObject != null)
          {
               field.set(context, referenceObject);
               result 		= true;
          }
       }
       catch (Exception e)
       {
           setFieldError(field, valueString, e);
       }
       return result;
   }
	
	public String getSimpleName()
	{
		return enumTypeSimpleName;
	}
	
	public ArrayList<String> getEnumStringConstants()
	{
		return enumStringConstants;
	}

	@Override
	public Enum getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
