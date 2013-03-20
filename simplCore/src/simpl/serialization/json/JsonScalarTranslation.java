package simpl.serialization.json;

import org.json.simple.JSONObject;

import simpl.interpretation.ScalarInterpretation;
import simpl.interpretation.SimplInterpretation;

public class JsonScalarTranslation implements JsonTranslationUnit{

	@Override
	public Class<?> getInterpClass() {
		// TODO Auto-generated method stub
		return ScalarInterpretation.class;
	}

	@Override
	public void appendToIR(SimplInterpretation si, JSONObject jsonrep) {
		ScalarInterpretation scalar = (ScalarInterpretation) si;
		jsonrep.put(scalar.fieldName, scalar.fieldValue);
	}

}
