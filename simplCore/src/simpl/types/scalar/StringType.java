package simpl.types.scalar;

import simpl.annotations.ScalarSupportFor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.ScalarType;

@ScalarSupportFor({String.class})
public class StringType extends ScalarType{

	// Marshalling a string isn't anything special: A string is a string on both ends. 
	@Override
	public String marshal(Object object) throws SIMPLTranslationException {
		return ((String)object);
	}

	@Override
	public Object unmarshal(String string) throws SIMPLTranslationException {
		return string;
	}

	@Override
	public Object getDefaultValue() {
		return "";
	}

}
