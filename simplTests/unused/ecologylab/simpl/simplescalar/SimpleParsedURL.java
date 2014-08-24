package ecologylab.simpl.simplescalar;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.annotations.simpl_scalar;
public class SimpleParsedURL {
	@simpl_scalar
	private ParsedURL simpleparsedurl;

	public ParsedURL getSimpleParsedURL(){ 
		 return this.simpleparsedurl;
	}

	public void setSimpleParsedURL(ParsedURL value){
		this.simpleparsedurl = value;
	}
}
