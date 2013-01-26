package legacy.tests.graph.diamond;

import simpl.annotations.dbal.simpl_composite;
import simpl.exceptions.SIMPLTranslationException;
import legacy.tests.TestCase;
import legacy.tests.TestingUtils;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

public class ClassD implements TestCase
{
	@simpl_composite
	private ClassA	classA;

	@simpl_composite
	private ClassB	classB;

	public ClassD()
	{

	}

	public ClassD(ClassA classA, ClassB classB)
	{
		this.setClassA(classA);
		this.setClassB(classB);
	}

	public void setClassA(ClassA classA)
	{
		this.classA = classA;
	}

	public ClassA getClassA()
	{
		return classA;
	}

	public void setClassB(ClassB classB)
	{
		this.classB = classB;
	}

	public ClassB getClassB()
	{
		return classB;
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		SimplTypesScope.enableGraphSerialization();

		ClassC classC = new ClassC();
		ClassD test = new ClassD(new ClassA(classC), new ClassB(classC));

		SimplTypesScope tScope = SimplTypesScope.get("classDTScope", ClassA.class, ClassB.class,
				ClassC.class, ClassD.class, ClassX.class);

		TestingUtils.test(test, tScope, Format.XML);
		TestingUtils.test(test, tScope, Format.JSON);
		TestingUtils.test(test, tScope, Format.TLV);

		SimplTypesScope.disableGraphSerialization();
	}
}
