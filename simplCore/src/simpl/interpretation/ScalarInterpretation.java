package simpl.interpretation;

import java.util.Set;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.EnumerationDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLDescriptionException;
import simpl.exceptions.SIMPLTranslationException;
import simpl.tools.ReflectionTools;
import simpl.types.ScalarType;
import simpl.types.TypeRegistry;

/**
 * Represents an interpretation of a scalar.
 * Since enumerations are effectively indistinguishable in format from the scalar, this also handles interpretation of enumerations
 *
 */
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
	
	public ScalarInterpretation() {
		// TODO Auto-generated constructor stub
	}

	public String toString()
	{
		return fieldName +"{"+this.scalarTypeName+"}" + "=["+ fieldValue + "]";
	}
	
	// Cache for the scalar type corresponding to this type name
	private ScalarType ourScalarType;
	private ScalarType getScalarType()
	{
		if(this.ourScalarType == null)
		{
			this.ourScalarType = TypeRegistry.getScalarType(this.scalarTypeName);
		}
		
		return this.ourScalarType;
	}
	
	private EnumerationDescriptor ourEnumerationDescriptor;
	private EnumerationDescriptor getEnumerationDescriptor(UnderstandingContext context)
	{
		if(this.ourEnumerationDescriptor == null)
		{
			try 
			{
				this.ourEnumerationDescriptor = context.getEnumerationDescriptor(this.scalarTypeName);
			} 
			catch (SIMPLTranslationException e) 
			{
				// Do nothing, returning null gets handled elsewhere.
			}
		}
		return this.ourEnumerationDescriptor;
	}
	
	/**
	 * Gets the type...
	 * @return True if it was a scalar, false if it was an enumeration, exception if it doesn't exist in the type registry or the sts
	 * @throws SIMPLTranslationException
	 */
	public boolean switchOnScalarOrEnumerationType(UnderstandingContext context) throws SIMPLTranslationException
	{
		this.getScalarType();
		
		if(this.ourScalarType == null)
		{
			this.getEnumerationDescriptor(context);
			
			if(this.ourEnumerationDescriptor == null)
			{
				throw new SIMPLTranslationException("Type doesn't exist! {"+this.scalarTypeName+ "}");
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
	public void resolve(Object context, Set<String> refSet, UnderstandingContext understandingContext) throws SIMPLTranslationException
	{

		if(this.switchOnScalarOrEnumerationType(understandingContext))
		{
			// Handle the scalar case
			try 
			{
				this.ourScalarType.setFieldValue(this.fieldValue, context.getClass().getField(this.fieldName), context);
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
		else
		{
			//handle the enum case:
			try 
			{
				Object value = this.getValue(context, refSet, understandingContext);
				ReflectionTools.setFieldValue(value, context.getClass().getField(this.fieldName), context);
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
	}
	
	
	
	@Override
	public Object getValue(Object context, Set<String> refSet,
			UnderstandingContext understandingContext)
			throws SIMPLTranslationException 
	{
		
		if(this.switchOnScalarOrEnumerationType(understandingContext))
		{
			return this.ourScalarType.unmarshal(this.fieldValue);
		}else{
			return this.ourEnumerationDescriptor.unmarshal(this.fieldValue);
		}
	}

	@Override
	public SimplInterpretation interpret(Object context, FieldDescriptor field,
			InterpretationContext interpretationContext)
			throws SIMPLTranslationException {

		String fieldName = field.getName();
		
		ScalarType st = field.getScalarType();
		if(st == null)
		{
			// Enumeration!
			EnumerationDescriptor ed = field.getEnumerationDescriptor();
			String value = ed.marshal(ReflectionTools.getFieldValue(field.getField(), context));
			
			String typeName = ed.getTagName();
			return new ScalarInterpretation(fieldName, value, typeName);
		}
		else
		{	
			// Get the value of the field's scalar; this allows us to marshal primitive types to string.
			String fieldValue = st.getFieldString(field.getField(), context);
			String typeName = st.getClass().getSimpleName();
			
			return new ScalarInterpretation(fieldName, fieldValue, typeName);
		}
	}

	@Override
	public SimplInterpretation interpretObject(Object theObject,
			InterpretationContext interpretationContext) throws SIMPLTranslationException {
		
		
		ScalarType st = TypeRegistry.getScalarType(theObject.getClass());
		if(st == null)
		{
			// enumeration!
			try
			{
				
				EnumerationDescriptor ed = EnumerationDescriptor.get(theObject.getClass());
				String value = ed.marshal(theObject);
				
				String typeName = ed.getTagName();
				return new ScalarInterpretation("", value, typeName);
			
			}catch(SIMPLDescriptionException sde)
			{
				throw new SIMPLTranslationException(sde);
			}
		}
		else
		{
			String value = st.marshal(theObject);
			String typeName = st.getClass().getSimpleName();
			return new ScalarInterpretation("", value, typeName);
		}
		
	}
}
