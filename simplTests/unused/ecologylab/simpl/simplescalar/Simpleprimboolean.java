package ecologylab.simpl.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
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
