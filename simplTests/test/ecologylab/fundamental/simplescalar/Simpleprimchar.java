package ecologylab.fundamental.simplescalar;
import simpl.annotations.dbal.simpl_scalar;
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
