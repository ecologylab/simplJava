package ecologylab.simpl.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
public class Simpleprimchar {
	@simpl_scalar
	private char simpleprimchar;

	public char getSimpleprimchar(){ 
		 return this.simpleprimchar;
	}

	public void setSimpleprimchar(char value){
		this.simpleprimchar = value;
	}
}
