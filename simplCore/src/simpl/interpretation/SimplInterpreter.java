package simpl.interpretation;

import java.util.LinkedList;
import java.util.List;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.ScalarType;

public class SimplInterpreter {

	public List<SimplInterpretation> interpretInstance(Object obj) throws SIMPLTranslationException
	{
		List<SimplInterpretation> list = new LinkedList<SimplInterpretation>();
		
		ClassDescriptor ourDescriptor =ClassDescriptors.getClassDescriptor(obj);
		
		for(FieldDescriptor fd : ourDescriptor.fields().Scalars)
		{
			ScalarType st = fd.getScalarType();
			if(st == null)
			{
				throw new SIMPLTranslationException("Null scalar type for field: " + fd.getName());
			}
			
			// Get the value of the field's scalar; this allows us to marshal primitive types to string.
			String fieldValue = st.getFieldString(fd.getField(), obj);
			String typeName = st.getClass().getSimpleName();
			String fieldName = fd.getName();
			
			list.add(new ScalarInterpretation(fieldName, fieldValue, typeName));
		}
		
		// Can't decide on this dichotomy; if this gets delegated nicely it'll not work this way at all. 
		for(FieldDescriptor fd : ourDescriptor.fields().ScalarCollections)
		{
			
		}
		
		
		return list;
	}
}
