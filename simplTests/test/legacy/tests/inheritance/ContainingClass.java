package legacy.tests.inheritance;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.formatenums.Format;

public class ContainingClass implements TestCase
{
	@simpl_classes(
	{ BaseClass.class, ChildClass1.class, ChildClass2.class })
	@simpl_composite
	BaseClass	theField;

	public ContainingClass()
	{
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		SimplTypesScope translationScope = SimplTypesScope.get("containingClassTScope", ContainingClass.class,
				ChildClass1.class, ChildClass2.class, BaseClass.class);

		ContainingClass ccb = new ContainingClass();
		ccb.theField = new BaseClass();

		TestingUtils.test(ccb, translationScope, Format.XML);
		TestingUtils.test(ccb, translationScope, Format.JSON);
		TestingUtils.test(ccb, translationScope, Format.TLV);

		ContainingClass cc1 = new ContainingClass();
		cc1.theField = new ChildClass1();

		TestingUtils.test(cc1, translationScope, Format.XML);
		TestingUtils.test(cc1, translationScope, Format.JSON);
		TestingUtils.test(cc1, translationScope, Format.TLV);

		ContainingClass cc2 = new ContainingClass();
		cc2.theField = new ChildClass2();
		
		TestingUtils.test(cc2, translationScope, Format.XML);
		TestingUtils.test(cc2, translationScope, Format.JSON);
		TestingUtils.test(cc2, translationScope, Format.TLV);

	}

	public void setTheField(BaseClass baseClass) {
		theField = baseClass;
		
	}
	
}
