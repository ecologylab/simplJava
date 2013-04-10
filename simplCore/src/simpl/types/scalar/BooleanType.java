package simpl.types.scalar;

import java.lang.reflect.Field;
import java.util.Collection;

import simpl.annotations.ScalarSupportFor;
import simpl.core.SimplIssue;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;
import simpl.types.ScalarType;


@ScalarSupportFor({Boolean.class, boolean.class})
public class BooleanType extends ScalarType{
	
	@Override
	public Object getDefaultValue()
	{
		return new Boolean(false);
	}
	
	@Override
	public String marshal(Object object) throws SIMPLTranslationException {
		Boolean ourBool = (Boolean)object;
		return ourBool.toString();
	}

	@Override
	public Object unmarshal(String string) throws SIMPLTranslationException {		
		
		String lcValue = string.toLowerCase();
		
		boolean trueValues = lcValue.equals("t") || lcValue.equals("true") || lcValue.equals("yes") || (lcValue.equals("1"));
		boolean falseValues = lcValue.isEmpty() || lcValue.equals("f") || lcValue.equals("false") || lcValue.equals("no") || (lcValue.equals("0"));
	
		if(!trueValues && !falseValues)
		{
			throw new SIMPLTranslationException(new SimplIssue("Value given is not supported",string,null));
		}

		if(trueValues)
		{
			return new Boolean(true);
		}
		else
		{
			return new Boolean(false);
		}		
	}
	
	@Override
	protected Object getBoxedValue(Field f, Object context) throws SIMPLTranslationException
	{
		boolean ourUnboxedValue = ReflectionTools.getFieldBooleanValue(f, context);
		return new Boolean(ourUnboxedValue);
	}
	
	@Override
	protected void setToUnboxedValue(Object boxedValue, Field f, Object context) throws SIMPLTranslationException 
	{
		Boolean ourBoolean = (Boolean)boxedValue;
		
		boolean ourUnboxedValue = ourBoolean.booleanValue();
		
		ReflectionTools.setFieldBooleanValue(ourUnboxedValue, f, context);
	}

}
