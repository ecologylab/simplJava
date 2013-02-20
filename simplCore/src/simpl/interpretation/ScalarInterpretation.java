package simpl.interpretation;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;

public class ScalarInterpretation implements SimplInterpretation{
	public String fieldName;
	public String fieldValue;
	
	public ScalarInterpretation(String name, String value)
	{
		this.fieldName = name;
		this.fieldValue = value;
	}
	
	public String toString()
	{
		return fieldName + "=["+ fieldValue + "]";
	}
	
	public void resolve(Object context, SimplRefCallbackMap updateMap, UnderstandingContext understandingContext) throws SIMPLTranslationException
	{
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(context.getClass());
		FieldDescriptor fd = cd.fields().by("name").get(this.fieldName);
		fd.getScalarType().setFieldValue(this.fieldValue, fd.getField(), context);
	}
	
	@Override
	public Object getValue(Object context, SimplRefCallbackMap callbackMap,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException {
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(context.getClass());
		FieldDescriptor fd = cd.fields().by("name").get(this.fieldName);
		return fd.getScalarType().unmarshal(this.fieldValue);
	}
}
