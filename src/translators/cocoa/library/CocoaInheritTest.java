package translators.cocoa.library;

import java.net.MalformedURLException;

import ecologylab.xml.ElementState;
import ecologylab.xml.XMLTranslationException;

/**
 * xml nested test class
 * 
 * @author nskhan
 */
public class CocoaInheritTest extends ElementState
{
	@simpl_composite
	private CocaTestClass	ct;

	@simpl_scalar
	private String				test;

	public CocoaInheritTest() throws MalformedURLException
	{
		ct = new CocaTestClass();
		test = "123";
	}

	public void setCt(CocaTestClass ct)
	{
		this.ct = ct;
	}

	public CocaTestClass getCt()
	{
		return ct;
	}

	public void setTest(String test)
	{
		this.test = test;
	}

	public String getTest()
	{
		return test;
	}

	public static void main(String args[]) throws MalformedURLException, XMLTranslationException
	{
		CocoaInheritTest t = new CocoaInheritTest();
		t.serialize(System.out);
	}
}
