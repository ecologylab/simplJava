package ecologylab.oodss.logging.translationScope;

import ecologylab.oodss.logging.MixedInitiativeOp;
import ecologylab.serialization.TranslationsClassProvider;

/**
 * Provide base log operation classes for translating a polymorphic list of Ops.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class MixedInitiativeOpClassesProvider extends TranslationsClassProvider
{
	public static final MixedInitiativeOpClassesProvider	STATIC_INSTANCE	= new MixedInitiativeOpClassesProvider();

	protected MixedInitiativeOpClassesProvider()
	{

	}

	/**
	 * @see ecologylab.serialization.TranslationsClassProvider#specificSuppliedClasses()
	 */
	@Override
	protected Class[] specificSuppliedClasses()
	{
		Class mixedInitiativeOpClasses[] =
		{ MixedInitiativeOp.class };

		return mixedInitiativeOpClasses;
	}
}
