package ecologylab.fundamental.simplescalar;
import simpl.annotations.dbal.simpl_scalar;
public class SimpleChar {
	@simpl_scalar
	private Character simplechar;

	public Character getSimpleChar(){ 
		 return this.simplechar;
	}

	public void setSimpleChar(Character value){
		this.simplechar = value;
	}
}
