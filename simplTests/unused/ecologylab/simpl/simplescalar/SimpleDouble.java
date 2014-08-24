package ecologylab.simpl.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
public class SimpleDouble {
	@simpl_scalar
	private Double simpledouble;

	public Double getSimpleDouble(){ 
		 return this.simpledouble;
	}

	public void setSimpleDouble(Double value){
		this.simpledouble = value;
	}
}
