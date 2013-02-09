package simpl.types.newStuff;

import java.awt.List;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import simpl.annotations.ScalarSupportFor;
import simpl.core.ISimplStringMarshaller;
import simpl.core.SimplIssue;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;

/**
 * An abstract class to represent a scalar type mapping in simpl.
 * Handles marshalling of Objects of the scalar type to the common simpl string representation.
 * (This common representation gets escaped in a format for serialization)
 * (The object returned from the common representation will get put into a given deserialized object
 * by another class.) 
 * @author tom
 *
 */
public abstract class ScalarType implements ISimplStringMarshaller{

	private boolean fieldIsPrimitive(Field f)
	{
		return f.getType().isPrimitive();
	}
	
	public String getFieldString(Field f, Object context) throws SIMPLTranslationException
	{
		checkTypeSupported(f.getType());
		
		Object fieldValue;
		if(fieldIsPrimitive(f))
		{
			fieldValue = getBoxedValue(f, context);
		}else{
			fieldValue = ReflectionTools.getFieldValue(f, context);
		}
		
		return marshal(fieldValue);
	}
	
	public void setFieldValue(String simplString, Field f, Object context) throws SIMPLTranslationException
	{
		checkTypeSupported(f.getType());
		
		Object boxedValue = unmarshal(simplString);
		
		if(fieldIsPrimitive(f))
		{
			setToUnboxedValue(boxedValue, f, context);
		}
		else
		{
			ReflectionTools.setFieldValue(boxedValue, f, context);
		}
	}
	
	@Override
	public abstract String marshal(Object object) throws SIMPLTranslationException;
	
	@Override
	public abstract Object unmarshal(String string) throws SIMPLTranslationException;
	
	protected Object getBoxedValue(Field f, Object context) throws SIMPLTranslationException
	{
		throw new RuntimeException("Get Boxed Value not implemented for base scalar types; fix your scalar type implementation to handle primitive types!");
	}
	
	protected void setToUnboxedValue(Object boxedValue, Field f, Object context) throws SIMPLTranslationException
	{
		throw new RuntimeException("SetToUnboxed value not implemented for base scalar types; fix your implementation to handle primitive scalar values;");
	}
	
	private Collection<Class<?>> supportCache = null;
	
	public Collection<Class<?>> getSupportedTypes() {
		if(supportCache == null)
		{
			ScalarSupportFor supports = getClass().getAnnotation(ScalarSupportFor.class);
			
			if(supports == null)
			{
				throw new RuntimeException("Scalar Type implementations must annotate with a ScalarSupportFor annotation!");
			}
			
			if(supports.value().length == 0)
			{
				throw new RuntimeException("SupportFor annotation must have at least one class in it!");
			}
		
			supportCache = Arrays.asList(supports.value());
		}
		return supportCache;
	}
	
	public void checkTypeSupported(Class<?> aClass)	throws SIMPLTranslationException
	{
		if(!this.getSupportedTypes().contains(aClass))
		{
			throw new SIMPLTranslationException("Type not supported by this scalar type: " + aClass.getName());
		}
	}
	
}
