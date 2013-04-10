package ecologylab.fundamental.simplescalar;
import simpl.annotations.dbal.simpl_scalar;
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
