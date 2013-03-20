package simpl.deserialization.json;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import simpl.core.ISimplTypesScope;
import simpl.descriptions.ClassDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.interpretation.CompositeInterpretation;
import simpl.interpretation.ScalarInterpretation;

public class JsonDeserializer {

	public JsonDeserializer()
	{
	}
	
	public CompositeInterpretation deserialize(String objRepr, ISimplTypesScope sts) throws SIMPLTranslationException
	{
		Object val = JSONValue.parse(objRepr);
		
		JSONObject jsonRepr = (JSONObject)val;
		
		if(jsonRepr.keySet().size() == 1)
		{
			Object rootDescriptor = jsonRepr.keySet().iterator().next();
			
			ClassDescriptor rootCD = sts.getClassDescriptorByTag((String)rootDescriptor);
			
			if(rootCD == null)
			{
				throw new SIMPLTranslationException("Class descriptor with tag name ["+(String)rootDescriptor+ "] not found in STS. Did you add it?");
			}
			
			CompositeInterpretation ci = new CompositeInterpretation(rootCD.getTagName());
			
			JSONObject innerObject = (JSONObject)jsonRepr.get(rootCD.getTagName());
			
			for(Object key : innerObject.keySet())
			{
				Object fieldValue = innerObject.get(key); // todo; handle polymorphic wrapping yo.
				
				if(rootCD.fields().Scalars.contains((String)key))
				{
					ci.addInterpretation(new ScalarInterpretation((String)key, (String)fieldValue, rootCD.fields().by("name").get((String)key).getScalarType().getClass().getSimpleName()));
				}				
			}
			
			return ci;
		}
		else
		{
			throw new SIMPLTranslationException("Malformed root object! Expecting only one key at the highest level.");
		}
		
		

	}
}
