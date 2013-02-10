package simpl.interpretation;

import java.util.HashMap;
import java.util.List;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;

public class SimplUnderstander {
// hurr. it's a prototype name, so its gonna be bad.
// an understander "understands" the interpretations derived from XML.

	public Object understandInterpretation(List<ScalarInterpretation> interps, ClassDescriptor cd) throws SIMPLTranslationException
	{
		Object ourObject = cd.getInstance();
		
		for(FieldDescriptor fd : cd.fieldDescriptors.Scalars)
		{
			//fd.setFieldToScalarDefault(ourObject, null);
		}
		
		HashMap<String, String> fieldNameToValues = new HashMap<String,String>();
		
		for(ScalarInterpretation si: interps) 
		{
			fieldNameToValues.put(si.fieldName, si.fieldValue);
		}
		
		for(FieldDescriptor fd: cd.fieldDescriptors.Scalars)
		{
			String value = fieldNameToValues.get(fd.getField().getName());
			//fd.getScalarType().setFieldValue(value, fd.getField(), ourObject);
		}
		
		return ourObject;
	}
}
