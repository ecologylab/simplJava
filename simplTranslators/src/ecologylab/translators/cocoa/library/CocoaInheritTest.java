package ecologylab.translators.cocoa.library;

import java.net.MalformedURLException;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.StringFormat;

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

	public static void main(String args[]) throws MalformedURLException, SIMPLTranslationException
	{
		CocoaInheritTest t = new CocoaInheritTest();
		SimplTypesScope.serialize(t, System.out, StringFormat.XML);
	}
}
