package simpl.serialization.json;

import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import simpl.exceptions.SIMPLTranslationException;
import simpl.interpretation.ListInterpretation;
import simpl.interpretation.SimplInterpretation;

public class JsonListTranslation implements JsonTranslationUnit{

	JsonSerializer jsonSer;
	
	public JsonListTranslation(JsonSerializer json)
	{
		this.jsonSer = json;
	}
	
	@Override
	public Class<?> getInterpClass() {
		return ListInterpretation.class;
	}

	@Override
	public void appendToIR(SimplInterpretation si, JSONObject jsonrep)
			throws SIMPLTranslationException {
		
		JSONObject obj = new JSONObject();
		JSONArray ja = new JSONArray();
		
		ListInterpretation la = (ListInterpretation)si;
		
		Integer i = 0;
		
		for(SimplInterpretation innersi : la.getInterpretations())
		{
			jsonSer.processTranslationUnit(innersi, obj);
			obj.put(i.toString(), obj.get(""));
			i++;
		}
		obj.remove("");
		
		for(Integer ij = 0; ij < obj.size(); ij++)
		{
			ja.add(ij, obj.get(ij.toString()));
		}
		
		jsonrep.put(la.getFieldName(), ja);
	}

}
