package simpl.deserialization.stringformats;

import java.io.File;

import simpl.core.DeserializationHookStrategy;
import simpl.core.ScalarUnmarshallingContext;
import simpl.core.SimplTypesScope;
import simpl.core.TranslationContext;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.deserialization.PullDeserializer;
import simpl.exceptions.SIMPLTranslationException;

import ecologylab.net.ParsedURL;

public abstract class StringPullDeserializer extends PullDeserializer
{

	public StringPullDeserializer(SimplTypesScope translationScope,
			TranslationContext translationContext)
	{
		super(translationScope, translationContext);
	}

	/**
	 * Constructs that creates a deserialization handler
	 * 
	 * @param translationScope
	 *          translation scope to use for de/serializing subsequent char sequences
	 * @param translationContext
	 *          used for graph handling
	 */
	public StringPullDeserializer(SimplTypesScope translationScope,
			TranslationContext translationContext, DeserializationHookStrategy deserializationHookStrategy)
	{
		super(translationScope, translationContext, deserializationHookStrategy);
	}	

	public abstract Object parse(CharSequence charSequence) throws SIMPLTranslationException;
	
	
	protected void SetAllScalarFieldsToDefault(Object context, ClassDescriptor cd, ScalarUnmarshallingContext scalarContext)
	{	
		for(FieldDescriptor fd: cd.allFieldDescriptors())
		{
			if(fd.isScalar())
			{
				//fd.setFieldToScalarDefault(context, scalarContext);
			}
		}
	}
}
