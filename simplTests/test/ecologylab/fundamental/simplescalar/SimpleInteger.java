package ecologylab.fundamental.simplescalar;
import simpl.annotations.dbal.simpl_scalar;
public class SimpleInteger {
	@simpl_scalar
	private Integer simpleinteger;

	public Integer getSimpleInteger(){ 
		 return this.simpleinteger;
	}

	public void setSimpleInteger(Integer value){
		this.simpleinteger = value;
	}
}
