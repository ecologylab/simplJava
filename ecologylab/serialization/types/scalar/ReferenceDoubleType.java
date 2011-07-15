package ecologylab.serialization.types.scalar;

import java.io.IOException;

import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.types.CrossLanguageTypeConstants;
import ecologylab.serialization.types.ScalarType;

public class ReferenceDoubleType extends ScalarType<Double>
implements CrossLanguageTypeConstants
{
	public ReferenceDoubleType()
	{
		super(Double.class, JAVA_DOUBLE, DOTNET_DOUBLE, OBJC_DOUBLE, null);
	}

	@Override
	public Double getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext) {
		return ("null".equalsIgnoreCase(value))?null:new Double(value);
	}

	
    /**
     * Get the value from the Field, in the context.
     * Append its value to the buffy.
     * 
     * @param buffy
     * @param field
     * @param context
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Override
    public void appendValue(StringBuilder buffy, FieldDescriptor f2xo, Object context) 
    throws IllegalArgumentException, IllegalAccessException
    {
        buffy.append(getValueToAppend(f2xo, context));
    }
    
    /**
     * Get the value from the Field, in the context.
     * Append its value to the buffy.
     * 
     * @param buffy
     * @param context
     * @param field
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Override
    public void appendValue(Appendable buffy, FieldDescriptor fieldDescriptor, Object context, TranslationContext serializationContext) 
    throws IllegalArgumentException, IllegalAccessException, IOException
    {
        buffy.append(getValueToAppend(fieldDescriptor, context));
    }
    
    public static String getValueToAppend(FieldDescriptor descriptor, Object context) 
    throws IllegalArgumentException, IllegalAccessException
    {
    	if (descriptor.getField() == null || descriptor.getField().get(context) == null)
    		return "null";
    	
    	return DoubleType.getValueToAppend(descriptor, context);
    }
}



