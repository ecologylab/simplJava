package simpl.types.scalar;

import java.lang.reflect.Field;

import simpl.annotations.ScalarSupportFor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;

@ScalarSupportFor({Short.class, short.class})
public class ShortType extends ScalarType {

	@Override
	public Object getDefaultValue()
	{
		return new Short((short)0);
	}
	
	@Override
	public String marshal(Object object) throws SIMPLTranslationException {
		return ((Short)object).toString();
	}

	@Override
	public Object unmarshal(String string) throws SIMPLTranslationException {		
		return Short.parseShort(string);
	}
	
	@Override
	protected Object getBoxedValue(Field f, Object context) throws SIMPLTranslationException
	{
		short unboxedValue = ReflectionTools.getFieldShortValue(f,context);
		return new Short(unboxedValue);
	}
	
	@Override
	protected void setToUnboxedValue(Object boxedValue, Field f, Object context) throws SIMPLTranslationException 
	{
		short unboxedValue = ((Short)boxedValue).shortValue();
		ReflectionTools.setFieldShortValue(unboxedValue, f, context);
	}
}
