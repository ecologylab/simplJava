package ecologylab.fundamental.simplescalar;
import simpl.annotations.dbal.simpl_scalar;
public class Simpleprimbyte {
	@simpl_scalar
	private byte simpleprimbyte;

	public byte getSimpleprimbyte(){ 
		 return this.simpleprimbyte;
	}

	public void setSimpleprimbyte(byte value){
		this.simpleprimbyte = value;
	}
}
