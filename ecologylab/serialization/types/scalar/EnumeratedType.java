package ecologylab.serialization.types.scalar;

import java.lang.reflect.Field;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.XMLTools;

public class EnumeratedType extends ReferenceType<Enum>
{

	public EnumeratedType()
	{
		super(Enum.class, null, null, null, null);
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

	@Override
	public Enum getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
