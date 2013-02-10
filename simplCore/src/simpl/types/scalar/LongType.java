package simpl.types.scalar;

import java.lang.reflect.Field;

import simpl.annotations.ScalarSupportFor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;

@ScalarSupportFor({Long.class, long.class})
public class LongType extends ScalarType{
	@Override
	public Object getDefaultValue()
	{
		return new Long(0);
	}
	
	@Override
	public String marshal(Object object) throws SIMPLTranslationException {
		return ((Long)object).toString();
	}

	@Override
	public Object unmarshal(String string) throws SIMPLTranslationException {		
		return new Long(Long.parseLong(string));
	}
	
	@Override
	protected Object getBoxedValue(Field f, Object context) throws SIMPLTranslationException
	{
		long unboxedValue = ReflectionTools.getFieldLongValue(f, context);
		return new Long(unboxedValue);
	}
	
	@Override
	protected void setToUnboxedValue(Object boxedValue, Field f, Object context) throws SIMPLTranslationException 
	{
		long unboxedValue = ((Long)boxedValue).longValue();
		ReflectionTools.setFieldLongValue(unboxedValue, f, context);
	}
}
