package legacy.tests.composite;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.formatenums.Format;

public class Container implements TestCase
{

	// @simpl_wrap
	@simpl_composite
	@simpl_classes(
	{ WCBase.class, WCSubOne.class, WCSubTwo.class })
	WCBase	wc;

	public Container()
	{
		wc = new WCBase(0);
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		Container c = new Container();

		c.wc = new WCSubTwo(true);

		SimplTypesScope containerTest = SimplTypesScope.get("containerTranslationscope",
				Container.class, WCBase.class, WCSubOne.class, WCSubTwo.class);
		

		TestingUtils.test(c, containerTest, Format.XML);
		TestingUtils.test(c, containerTest, Format.JSON);

	}
}
