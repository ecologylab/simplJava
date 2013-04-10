package simpl.types.scalar;

import java.lang.reflect.Field;

import simpl.annotations.ScalarSupportFor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;
import simpl.types.ScalarType;

@ScalarSupportFor({Float.class, float.class})
public class FloatType extends ScalarType{
	@Override
	public Object getDefaultValue()
	{
		return new Float(0.0);
	}
	
	@Override
	public String marshal(Object object) throws SIMPLTranslationException {
		return ((Float)object).toString();
	}

	@Override
	public Object unmarshal(String string) throws SIMPLTranslationException {		
		return new Float(Float.parseFloat(string));
	}	
	
	@Override
	protected Object getBoxedValue(Field f, Object context) throws SIMPLTranslationException
	{
		float unboxedValue = ReflectionTools.getFieldFloatValue(f,context);
		return new Float(unboxedValue);
	}
	
	@Override
	protected void setToUnboxedValue(Object boxedValue, Field f, Object context) throws SIMPLTranslationException 
	{
		float unboxedValue = ((Float)boxedValue).floatValue();
		ReflectionTools.setFieldFloatValue(unboxedValue, f, context);
	}
}
