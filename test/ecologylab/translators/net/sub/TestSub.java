package ecologylab.translators.net.sub;

import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.translators.net.TestBase;

@simpl_inherit
public class TestSub extends TestBase
{

	@simpl_composite
	private TestBase composite1;
	
}
