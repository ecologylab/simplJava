package legacy.tests.circle;

import simpl.annotations.dbal.simpl_scalar;
import simpl.exceptions.SIMPLTranslationException;
import legacy.tests.TestCase;
import legacy.tests.TestingUtils;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

public class Point implements TestCase
{

	@simpl_scalar
	private int	x;

	@simpl_scalar
	private int	y;

	public Point()
	{

	}

	public Point(int x, int y)
	{
		this.setX(x);
		this.setY(y);
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

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		Point p = new Point(1, 2);
		SimplTypesScope scope = SimplTypesScope.get("pointTScope", Point.class);
		
//		TestingUtils.generateCocoaClasses(scope);
		
		SimplTypesScope.enableGraphSerialization();
		TestingUtils.serializeSimplTypesScope(scope, "Point", Format.JSON);
		
		TestingUtils.test(p, scope, Format.XML);
		TestingUtils.test(p, scope, Format.JSON);		
		TestingUtils.test(p, scope, Format.TLV);
	}
}
