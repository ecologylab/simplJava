package simpl.types.newStuff;

import java.lang.reflect.Field;

import simpl.annotations.ScalarSupportFor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;

@ScalarSupportFor({Byte.class, byte.class})
public class ByteType extends ScalarType{
	
	@Override
	public Object getDefaultValue()
	{
		return new Byte((byte) 0);
	}
	
	@Override
	public String marshal(Object object) throws SIMPLTranslationException 
	{
		Byte ourByte = (Byte)object;
		return ourByte.toString();
	}

	@Override
	public Object unmarshal(String string) throws SIMPLTranslationException 
	{		
		return Byte.parseByte(string);
	}
	
	@Override
	protected Object getBoxedValue(Field f, Object context) throws SIMPLTranslationException
	{
		byte unboxedValue = ReflectionTools.getFieldByteValue(f, context);
		Byte boxedValue = new Byte(unboxedValue);
		return boxedValue;
	}
	
	@Override
	protected void setToUnboxedValue(Object boxedValue, Field f, Object context) throws SIMPLTranslationException 
	{
		byte unboxedValue = ((Byte)boxedValue).byteValue();
		ReflectionTools.setFieldByteValue(unboxedValue, f,context);
	}
}
