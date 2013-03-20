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
			
			if(innerObject.keySet().contains("simpl.id") && innerObject.keySet().contains("simpl.ref"))
			{
				throw new SIMPLTranslationException("Cannot contain both an ID and a Ref!");
			}
			
			if(innerObject.keySet().contains("simpl.ref"))
			{
				if(innerObject.keySet().size() > 1)
				{
					throw new SIMPLTranslationException("References should not have additional contents!");
				}
				
				ci.setRefString((String)innerObject.get("simpl.ref"));
			}
			
			if(innerObject.keySet().contains("simpl.id"))
			{
				String id = (String)innerObject.get("simpl.id");
				ci.setIDString(id);
			}
			
			for(Object key : innerObject.keySet())
			{
				String keyVal = (String) key;
				
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
