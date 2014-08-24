package ecologylab.simpl.simplescalar;
import java.util.UUID;
import ecologylab.serialization.annotations.simpl_scalar;
public class SimpleUUID {
	@simpl_scalar
	private UUID simpleuuid;

	public UUID getSimpleUUID(){ 
		 return this.simpleuuid;
	}

	public void setSimpleUUID(UUID value){
		this.simpleuuid = value;
	}
}
