package ecologylab.fundamental.simplescalar;
import simpl.annotations.dbal.simpl_scalar;
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
