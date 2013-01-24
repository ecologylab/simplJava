package ecologylab.fundamental.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
public class Simpleprimshort {
	@simpl_scalar
	private short simpleprimshort;

	public short getSimpleprimshort(){ 
		 return this.simpleprimshort;
	}

	public void setSimpleprimshort(short value){
		this.simpleprimshort = value;
	}
}
