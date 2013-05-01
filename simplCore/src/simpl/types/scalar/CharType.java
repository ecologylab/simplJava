package simpl.types.scalar;

import java.lang.reflect.Field;

import simpl.annotations.ScalarSupportFor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;
import simpl.types.ScalarType;

@ScalarSupportFor({Character.class, char.class})
public class CharType extends RegisteredScalarType{

	@Override
	public Object getDefaultValue()
	{
		return ' ';
	}
	
	@Override
	public String marshal(Object object) throws SIMPLTranslationException 
	{
		return ((Character)object).toString();
	}

	@Override
	public Object unmarshal(String string) throws SIMPLTranslationException {		
		return new Character(string.charAt(0));
	}
	
	@Override
	protected Object getBoxedValue(Field f, Object context) throws SIMPLTranslationException
	{
		char unboxedValue = ReflectionTools.getFieldCharValue(f, context);
		return new Character(unboxedValue);
	}
	
	@Override
	protected void setToUnboxedValue(Object boxedValue, Field f, Object context) throws SIMPLTranslationException 
	{
		char unboxedValue = ((Character)boxedValue).charValue();
		ReflectionTools.setFieldCharValue(unboxedValue, f, context);
	}
}
