package ecologylab.serialization.types.scalar;

import java.io.IOException;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.serializers.enums.Format;
import ecologylab.serialization.types.ScalarType;

/*
 * This is not being used as composite are only treated as scalars in a certain context of serialization. for example bibtex 
 * but not in XML. 
 */
@simpl_inherit
public class CompositeAsScalarType<T> extends ScalarType<T>
{
	public CompositeAsScalarType()
	{
		super((Class<? extends T>) CompositeAsScalarType.class, null, null, null);
	}

	/**
	 * Append the String directly, unless it needs escaping, in which case, call escapeXML.
	 * 
	 * @param instance
	 * @param buffy
	 * @param needsEscaping
	 */
	@Override
	public void appendValue(T instance, StringBuilder buffy, boolean needsEscaping, TranslationContext serializationContext)
	{
		if(instance instanceof ElementState)
		{
			ClassDescriptor compositeElement = ClassDescriptor.getClassDescriptor((ElementState) instance);
			FieldDescriptor scalarValueFD = compositeElement.getScalarValueFieldDescripotor();
			if(scalarValueFD != null)
			{
				try
				{
					scalarValueFD.getScalarType().appendValue(buffy, scalarValueFD, instance);
				}
				catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	/**
	 * Append the String directly, unless it needs escaping, in which case, call escapeXML.
	 * 
	 * @param instance
	 * @param appendable
	 * @param needsEscaping
	 * @throws IOException 
	 */
	@Override
	public void appendValue(T instance, Appendable appendable, boolean needsEscaping, TranslationContext serializationContext, Format format) 
	throws IOException
	{
		if(instance instanceof ElementState)
		{
			ClassDescriptor compositeElement = ClassDescriptor.getClassDescriptor((ElementState) instance);
			FieldDescriptor scalarValueFD = compositeElement.getScalarValueFieldDescripotor();
			if(scalarValueFD != null)
			{
				try
				{
					scalarValueFD.getScalarType().appendValue(appendable, scalarValueFD, instance, null, format);
				}
				catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


	@Override
	public T getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
