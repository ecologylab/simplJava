package ecologylab.simpl.simplescalar;
import java.util.Date;
import ecologylab.serialization.annotations.simpl_scalar;
public class SimpleDate {
	@simpl_scalar
	private Date simpledate;

	public Date getSimpleDate(){ 
		 return this.simpledate;
	}

	public void setSimpleDate(Date value){
		this.simpledate = value;
	}
}
