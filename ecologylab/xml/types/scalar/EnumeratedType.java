package ecologylab.xml.types.scalar;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import ecologylab.xml.FieldDescriptor;
import ecologylab.xml.ScalarUnmarshallingContext;
import ecologylab.xml.XMLTools;

public class EnumeratedType extends ReferenceType<Enum>
{

	public EnumeratedType()
	{
		super(Enum.class);
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

	@Override
	public String getCSharptType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDbType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveCType()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
