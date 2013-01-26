package legacy.tests.circle;

import java.io.File;
import java.io.IOException;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.SimplTypesScope;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;


public class Circle implements TestCase
{
	@simpl_hints(Hint.XML_LEAF)
	@simpl_scalar
	int		radius;
	
	@simpl_composite
	Point	center;

	public Circle()
	{
	}

	public Circle(int radius, Point center)
	{
		this.radius = radius;
		this.center = center;
	}

	public Circle(int radius, int x, int y)
	{
		this.radius = radius;
		this.center = new Point(x, y);
	}
	
	@Override
	public void runTest() throws SIMPLTranslationException
	{
		Circle c = new Circle(3, 2, 1);
		
		
		SimplTypesScope t = SimplTypesScope.get("circleTScope", Circle.class, Point.class);
				
		SimplTypesScope.enableGraphSerialization();

		TestingUtils.test(c, SimplTypesScope.get("circle", Circle.class, Point.class), Format.XML);
		
		TestingUtils.test(c, SimplTypesScope.get("circle", Circle.class, Point.class), Format.JSON);
		TestingUtils.test(c, SimplTypesScope.get("circle", Circle.class, Point.class), Format.TLV);
	}
}
