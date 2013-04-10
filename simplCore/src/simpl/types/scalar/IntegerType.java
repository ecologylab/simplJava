package simpl.types.scalar;

import java.lang.reflect.Field;

import simpl.annotations.ScalarSupportFor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;
import simpl.types.ScalarType;

@ScalarSupportFor({Integer.class, int.class})
public class IntegerType extends ScalarType{
	
	@Override
	public Object getDefaultValue()
	{
		return new Integer(0);
	}
	
	@Override
	public String marshal(Object object) throws SIMPLTranslationException {
		return ((Integer)object).toString();
	}

	@Override
	public Object unmarshal(String string) throws SIMPLTranslationException {		
		return new Integer(Integer.parseInt(string));
	}
	
	@Override
	protected Object getBoxedValue(Field f, Object context) throws SIMPLTranslationException
	{
		int unboxedValue = ReflectionTools.getFieldIntValue(f, context);
		return new Integer(unboxedValue);
	}
	
	@Override
	protected void setToUnboxedValue(Object boxedValue, Field f, Object context) throws SIMPLTranslationException 
	{
		int unboxedValue = ((Integer)boxedValue).intValue();
		ReflectionTools.setFieldIntValue(unboxedValue, f, context);
	}
}
