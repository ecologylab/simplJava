package simpl.interpretation;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.ScalarType;
import simpl.types.TypeRegistry;

public class ScalarInterpretation implements SimplInterpretation{
	
	public String fieldName;
	public String fieldValue;
	public String scalarTypeName;
	
	public ScalarInterpretation(String name, String value, String scalarTypeName)
	{
		this.fieldName = name;
		this.fieldValue = value;
		this.scalarTypeName = scalarTypeName;
	}
	
	public String toString()
	{
		return fieldName +"{"+this.scalarTypeName+"}" + "=["+ fieldValue + "]";
	}
	
	// Cache for the scalar type corresponding to this type name
	private ScalarType ourScalarType;
	private ScalarType getScalarType() throws SIMPLTranslationException
	{
		if(this.ourScalarType == null)
		{
			this.ourScalarType = TypeRegistry.getScalarType(this.scalarTypeName);
			
			if(this.ourScalarType == null)
			{
				throw new SIMPLTranslationException("Scalar type {"+this.scalarTypeName+"} is not in the TypeRegistry! Did you forget to statically initialize and register this type?");
			}
		}
		
		return this.ourScalarType;
	}
	
	public void resolve(Object context, SimplRefCallbackMap updateMap, UnderstandingContext understandingContext) throws SIMPLTranslationException
	{
		ScalarType scalarType = getScalarType();

		try 
		{
			scalarType.setFieldValue(this.fieldValue, context.getClass().getField(this.fieldName), context);
		}
		catch (NoSuchFieldException e) 
		{
			throw new SIMPLTranslationException(e);
		} 
		catch (SecurityException e) 
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	@Override
	public Object getValue(Object context, SimplRefCallbackMap callbackMap,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException {
		ScalarType st = getScalarType();
		return st.unmarshal(this.fieldValue);
	}
}
