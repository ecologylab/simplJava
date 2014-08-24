package ecologylab.simpl.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
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
