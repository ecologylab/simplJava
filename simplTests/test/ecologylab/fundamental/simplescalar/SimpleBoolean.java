package ecologylab.fundamental.simplescalar;

import simpl.annotations.dbal.simpl_scalar;

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
	