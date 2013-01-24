package ecologylab.fundamental.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
public class SimpleBoolean {
	@simpl_scalar
	private Boolean simpleboolean;

	public Boolean getSimpleBoolean(){ 
		 return this.simpleboolean;
	}

	public void setSimpleBoolean(Boolean value){
		this.simpleboolean = value;
	}
}
