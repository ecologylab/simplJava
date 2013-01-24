package ecologylab.fundamental.simplescalar;
import java.net.URL;
import ecologylab.serialization.annotations.simpl_scalar;
public class SimpleJavaURL {
	@simpl_scalar
	private URL simplejavaurl;

	public URL getSimpleJavaURL(){ 
		 return this.simplejavaurl;
	}

	public void setSimpleJavaURL(URL value){
		this.simplejavaurl = value;
	}
}
