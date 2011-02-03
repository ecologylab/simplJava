package ecologylab.serialization.types.scalar;

import java.io.IOException;

import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;

public class ReferenceBooleanType extends ScalarType<Boolean>
{
	public ReferenceBooleanType()
	{
		super(Boolean.class);
	}

	@Override
	public String getCSharptType() {
		return MappingConstants.DOTNET_BOOLEAN;
	}

	@Override
	public String getDbType() {
		return null;
	}

	@Override
	public Boolean getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext) {
		return ("null".equalsIgnoreCase(value))?null:new Boolean(value);
	}

	@Override
	public String getObjectiveCType() {
		return MappingConstants.OBJC_BOOLEAN;
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
    	
    	return BooleanType.getValueToAppend(descriptor, context);
    }

}
