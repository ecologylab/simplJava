package simpl.deserialization.oldsimpl;

import java.io.File;

import simpl.core.DeserializationHookStrategy;
import simpl.core.ISimplTypesScope;
import simpl.core.ScalarUnmarshallingContext;
import simpl.core.TranslationContext;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;


public abstract class StringPullDeserializer extends PullDeserializer
{

	public StringPullDeserializer(ISimplTypesScope translationScope,
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
	public StringPullDeserializer(ISimplTypesScope translationScope,
			TranslationContext translationContext, DeserializationHookStrategy deserializationHookStrategy)
	{
		super(translationScope, translationContext, deserializationHookStrategy);
	}	

	public abstract Object parse(CharSequence charSequence) throws SIMPLTranslationException;
	
	
	protected void SetAllScalarFieldsToDefault(Object context, ClassDescriptor cd, ScalarUnmarshallingContext scalarContext) throws SIMPLTranslationException
	{	
		for(FieldDescriptor fd: cd.allFieldDescriptors())
		{
			if(fd.isScalar())
			{
				fd.setFieldToScalarDefault(context, scalarContext);
			}
		}
	}
}
