package ecologylab.fundamental.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
public class Simpleprimlong {
	@simpl_scalar
	private long simpleprimlong;

	public long getSimpleprimlong(){ 
		 return this.simpleprimlong;
	}

	public void setSimpleprimlong(long value){
		this.simpleprimlong = value;
	}
}
