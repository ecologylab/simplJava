package legacy.tests.graph;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.SimplTypesScope;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;
import legacy.tests.TestCase;
import legacy.tests.TestingUtils;

@simpl_inherit
public class ClassA implements TestCase
{
	@simpl_scalar
	private int			x;

	@simpl_scalar
	private int			y;

	@simpl_composite
	private ClassB	classB;

	@simpl_composite
	private ClassA	classA;

	public ClassA()
	{

	}

	public ClassA(int x, int y)
	{
		setX(x);
		setY(y);
		setClassA(this);
	}

	public ClassA(int x, int y, ClassB classB)
	{
		setX(x);
		setY(y);
		setClassB(classB);
		setClassA(this);
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getX()
	{
		return x;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getY()
	{
		return y;
	}

	public void setClassB(ClassB classB)
	{
		this.classB = classB;
	}

	public ClassB getClassB()
	{
		return classB;
	}

	public void setClassA(ClassA classA)
	{
		this.classA = classA;
	}

	public ClassA getClassA()
	{
		return classA;
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		SimplTypesScope.enableGraphSerialization();

		ClassA test = new ClassA(1, 2);
		ClassB classB = new ClassB(3, 4, test);

		test.setClassB(classB);

		SimplTypesScope tScope = SimplTypesScope.get("classATScope", ClassA.class, ClassB.class);
		
//		TestingUtils.generateCocoaClasses(tScope);

		TestingUtils.test(test, tScope, Format.XML);
		TestingUtils.test(test, tScope, Format.JSON);
		TestingUtils.test(test, tScope, Format.TLV);

		SimplTypesScope.disableGraphSerialization();
	}
}
