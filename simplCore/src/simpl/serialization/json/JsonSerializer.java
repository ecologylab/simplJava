package simpl.serialization.json;


import java.util.HashMap;
import java.util.Map;

import org.json.simple.*;

import simpl.exceptions.SIMPLTranslationException;
import simpl.interpretation.CompositeInterpretation;
import simpl.interpretation.SimplInterpretation;
import simpl.interpretation.SimplInterpreter;

public class JsonSerializer {
	
	
	private Map<Class<?>, JsonTranslationUnit> translators;
	
	public JsonSerializer()
	{
		translators = new HashMap<Class<?>, JsonTranslationUnit>();
		registerTranslator(new JsonScalarTranslation());
		registerTranslator(new JsonCompositeTranslation(this)); // pass this a reference to the json serializer b/c it needs to processTranslationUnits
		registerTranslator(new JsonListTranslation(this));
	}
	
	private void registerTranslator(JsonTranslationUnit jtu)
	{
		translators.put(jtu.getInterpClass(), jtu);
	}
	
	public void processTranslationUnit(SimplInterpretation si, JSONObject obj) throws SIMPLTranslationException
	{
		JsonTranslationUnit jtu = translators.get(si.getClass());
		if(jtu == null)
		{
			throw new SIMPLTranslationException("Does not contain translation unit for interpretation type: " + si.getClass().getName());
		}
		
		jtu.appendToIR(si, obj);
	}
	
	public String serialize(SimplInterpretation interp) throws SIMPLTranslationException
	{	
		if(!(interp instanceof CompositeInterpretation))
		{
			throw new SIMPLTranslationException("Root object must be a composite interpretation!"); 
		}
		
		JSONObject jsonObj = new JSONObject();
		
		processTranslationUnit(interp, jsonObj);
		
		return jsonObj.toString();
	}
}
