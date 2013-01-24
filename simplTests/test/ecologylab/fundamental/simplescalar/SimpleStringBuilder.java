package ecologylab.fundamental.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
public class SimpleStringBuilder {
	@simpl_scalar
	private StringBuilder simplestringbuilder;

	public StringBuilder getSimpleStringBuilder(){ 
		 return this.simplestringbuilder;
	}

	public void setSimpleStringBuilder(StringBuilder value){
		this.simplestringbuilder = value;
	}
}
