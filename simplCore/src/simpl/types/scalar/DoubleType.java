package simpl.types.scalar;

import java.lang.reflect.Field;

import simpl.annotations.ScalarSupportFor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;
import simpl.types.ScalarType;

@ScalarSupportFor({Double.class, double.class})
public class DoubleType extends ScalarType {
	@Override
	public Object getDefaultValue()
	{
		return new Double(0.0);
	}
	
	@Override
	public String marshal(Object object) throws SIMPLTranslationException {
		return ((Double)object).toString();
	}

	@Override
	public Object unmarshal(String string) throws SIMPLTranslationException {		
		return new Double(Double.parseDouble(string));
	}
	
	@Override
	protected Object getBoxedValue(Field f, Object context) throws SIMPLTranslationException
	{
		double unboxedValue = ReflectionTools.getFieldDoubleValue(f, context);
		return new Double(unboxedValue);
	}
	
	@Override
	protected void setToUnboxedValue(Object boxedValue, Field f, Object context) throws SIMPLTranslationException 
	{
		double unboxedValue = ((Double)boxedValue).doubleValue();
		ReflectionTools.setFieldDoubleValue(unboxedValue, f, context);
	}
}
