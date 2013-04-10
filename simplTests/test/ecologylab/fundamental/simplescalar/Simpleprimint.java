package ecologylab.fundamental.simplescalar;
import simpl.annotations.dbal.simpl_scalar;
public class Simpleprimint {
	@simpl_scalar
	private int simpleprimint;

	public int getSimpleprimint(){ 
		 return this.simpleprimint;
	}

	public void setSimpleprimint(int value){
		this.simpleprimint = value;
	}
}
