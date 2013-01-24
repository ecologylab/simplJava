package ecologylab.fundamental.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
public class SimpleShort {
	@simpl_scalar
	private Short simpleshort;

	public Short getSimpleShort(){ 
		 return this.simpleshort;
	}

	public void setSimpleShort(Short value){
		this.simpleshort = value;
	}
}
