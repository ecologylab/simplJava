package legacy.tests.circle;

import java.util.ArrayList;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.Format;

public class CollectionOfCircles implements TestCase
{
//	@simpl_nowrap
	@simpl_collection("circles")
	private ArrayList<Circle>	collectionOfCircles;

	@simpl_hints(Hint.XML_LEAF)
	@simpl_scalar
	int												yo	= 1;

	public CollectionOfCircles()
	{
		collectionOfCircles = new ArrayList<Circle>();
	}

	public void addCircle(int r, int x, int y)
	{
		collectionOfCircles.add(new Circle(r, x, y));
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		CollectionOfCircles coc = new CollectionOfCircles();

		coc.addCircle(1, 2, 3);
		coc.addCircle(1, 2, 4);
		coc.addCircle(1, 2, 5);
		coc.addCircle(1, 2, 6);
		coc.addCircle(1, 2, 7);

		SimplTypesScope circleTranslationScope = SimplTypesScope.get("collectionOfCirclesTScope",
				CollectionOfCircles.class, Circle.class, Point.class);
		
		
//		TestingUtils.generateCocoaClasses(circleTranslationScope);

		TestingUtils.test(coc, circleTranslationScope, Format.XML);

		TestingUtils.test(coc, circleTranslationScope, Format.JSON);
		TestingUtils.test(coc, circleTranslationScope, Format.TLV);
	}
	
	public int getYo(){
		return yo;
	}
	
	public ArrayList<Circle> getCollection(){
		return collectionOfCircles;
	}
}
