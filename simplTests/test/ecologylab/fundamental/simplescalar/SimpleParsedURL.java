package ecologylab.fundamental.simplescalar;
import simpl.annotations.dbal.simpl_scalar;
import ecologylab.net.ParsedURL;
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
