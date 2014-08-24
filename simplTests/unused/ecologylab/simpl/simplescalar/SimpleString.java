package ecologylab.simpl.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
public class SimpleString {
	@simpl_scalar
	private String simplestring;

	public String getSimpleString(){ 
		 return this.simplestring;
	}

	public void setSimpleString(String value){
		this.simplestring = value;
	}
}
