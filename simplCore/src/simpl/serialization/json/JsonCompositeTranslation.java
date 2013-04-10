package simpl.serialization.json;

import org.json.simple.JSONObject;

import simpl.exceptions.SIMPLTranslationException;
import simpl.interpretation.CompositeInterpretation;
import simpl.interpretation.SimplInterpretation;

public class JsonCompositeTranslation implements JsonTranslationUnit {

	private final JsonSerializer jsonSer;
	
	public JsonCompositeTranslation(JsonSerializer json)
	{
		this.jsonSer = json;
	}
	@Override
	public Class<?> getInterpClass() {
		// TODO Auto-generated method stub
		return CompositeInterpretation.class;
	}

	@Override
	public void appendToIR(SimplInterpretation si, JSONObject jsonrep) throws SIMPLTranslationException {
		
		CompositeInterpretation comp = (CompositeInterpretation)si;
		
		JSONObject compObj = new JSONObject();
		
		if(comp.fieldName != null && !comp.fieldName.isEmpty())
		{
			// Fieldname for non-polymorphic composites
			jsonrep.put(comp.fieldName, compObj); 
			// todo: fix this logic for polymorphic; may need to override interp underlying. :3 
		}
		else
		{
			// Just put the tag name; this is a root node. 
			jsonrep.put(comp.tagName, compObj);
		}
		
		if(comp.getRefString() != null && !comp.getRefString().isEmpty())
		{
			compObj.put("simpl.ref", comp.getRefString());
		}
		else
		{	
			if(comp.getIDString() != null && !comp.getIDString().isEmpty())
			{
				compObj.put("simpl.id", comp.getIDString());
			}
					
			for(SimplInterpretation compInterp: comp.getInterpretations())
			{
				jsonSer.processTranslationUnit(compInterp, compObj);
			}
		}
	}
}
