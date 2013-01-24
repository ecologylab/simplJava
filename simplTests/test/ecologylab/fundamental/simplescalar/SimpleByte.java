package ecologylab.fundamental.simplescalar;
import ecologylab.serialization.annotations.simpl_scalar;
public class SimpleByte {
	@simpl_scalar
	private Byte simplebyte;

	public Byte getSimpleByte(){ 
		 return this.simplebyte;
	}

	public void setSimpleByte(Byte value){
		this.simplebyte = value;
	}
}
