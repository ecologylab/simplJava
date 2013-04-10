package ecologylab.fundamental.simplescalar;
import simpl.annotations.dbal.simpl_scalar;
public class SimpleFloat {
	@simpl_scalar
	private Float simplefloat;

	public Float getSimpleFloat(){ 
		 return this.simplefloat;
	}

	public void setSimpleFloat(Float value){
		this.simplefloat = value;
	}
}
