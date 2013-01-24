package ecologylab.fundamental.simplescalar;
import java.util.regex.Pattern;
import ecologylab.serialization.annotations.simpl_scalar;
public class SimplePattern {
	@simpl_scalar
	private Pattern simplepattern;

	public Pattern getSimplePattern(){ 
		 return this.simplepattern;
	}

	public void setSimplePattern(Pattern value){
		this.simplepattern = value;
	}
}
