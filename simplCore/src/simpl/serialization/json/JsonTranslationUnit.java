package simpl.serialization.json;

import org.json.simple.JSONObject;

import simpl.exceptions.SIMPLTranslationException;
import simpl.interpretation.SimplInterpretation;

public interface JsonTranslationUnit {
	Class<?> getInterpClass();
	void appendToIR(SimplInterpretation si, JSONObject jsonrep) throws SIMPLTranslationException;
}
