package legacy.tests.scalar;

import java.util.ArrayList;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.formatenums.Format;

public class ScalarCollection implements TestCase
{
	@simpl_nowrap
	@simpl_collection("circles")
	private ArrayList<Integer>	collectionOfIntegers;

	public ScalarCollection()
	{
		collectionOfIntegers = new ArrayList<Integer>();
	}

	public void addInt(int integer)
	{
		collectionOfIntegers.add(integer);
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		ScalarCollection sc = new ScalarCollection();

		sc.addInt(1);
		sc.addInt(2);
		sc.addInt(3);
		sc.addInt(4);
		sc.addInt(5);

		SimplTypesScope scalarCollectionTranslationScope = SimplTypesScope.get(
				"scalarCollectionTScope", ScalarCollection.class);
		
		TestingUtils.test(sc, scalarCollectionTranslationScope, Format.XML);

		TestingUtils.test(sc, scalarCollectionTranslationScope, Format.JSON);
		TestingUtils.test(sc, scalarCollectionTranslationScope, Format.TLV);
	}
	
	public ArrayList<Integer> getCollection(){
		return collectionOfIntegers;
	}
}