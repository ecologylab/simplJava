package ecologylab.fundamental.simplescalar;
import simpl.annotations.dbal.simpl_scalar;
public class SimpleLong {
	@simpl_scalar
	private Long simplelong;

	public Long getSimpleLong(){ 
		 return this.simplelong;
	}

	public void setSimpleLong(Long value){
		this.simplelong = value;
	}
}
