package ecologylab.simpl.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
public class Simpleprimdouble {
	@simpl_scalar
	private double simpleprimdouble;

	public double getSimpleprimdouble(){ 
		 return this.simpleprimdouble;
	}

	public void setSimpleprimdouble(double value){
		this.simpleprimdouble = value;
	}
}
