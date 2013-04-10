package ecologylab.fundamental.simplescalar;
import simpl.annotations.dbal.simpl_scalar;
public class Simpleprimboolean {
	@simpl_scalar
	private boolean simpleprimboolean;

	public boolean getSimpleprimboolean(){ 
		 return this.simpleprimboolean;
	}

	public void setSimpleprimboolean(boolean value){
		this.simpleprimboolean = value;
	}
}
