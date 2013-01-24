package ecologylab.fundamental.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
public class Simpleprimfloat {
	@simpl_scalar
	private float simpleprimfloat;

	public float getSimpleprimfloat(){ 
		 return this.simpleprimfloat;
	}

	public void setSimpleprimfloat(float value){
		this.simpleprimfloat = value;
	}
}
