package legacy.tests.scalar;

import java.util.ArrayList;

import simpl.annotations.dbal.FieldUsage;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_scalar;
import simpl.exceptions.SIMPLTranslationException;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;

import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

public class EnumOfFieldUsageCollection implements TestCase{//Test no render
	@simpl_scalar
	int number;
	
	@simpl_collection("numnum")
	ArrayList<FieldUsage>	collectionOfFieldUseage;
	
	public EnumOfFieldUsageCollection()
	{
		number = 4;
	}
	
	public EnumOfFieldUsageCollection(ArrayList<FieldUsage> ce) {
		collectionOfFieldUseage = ce;
		number = 4;
	}

	@Override
	public void runTest() throws SIMPLTranslationException {

		ArrayList<FieldUsage> ce = new ArrayList<FieldUsage>();
		ce.add(FieldUsage.CODE_GENERATION);
		ce.add(FieldUsage.PERSISTENCE);
		
		EnumOfFieldUsageCollection c = new EnumOfFieldUsageCollection(ce);
		
		SimplTypesScope t = SimplTypesScope.get("enumCollectionTest",EnumOfFieldUsageCollection.class);		
		SimplTypesScope.enableGraphSerialization();

		TestingUtils.test(c, t, Format.XML);
		TestingUtils.test(c, t, Format.JSON);
		TestingUtils.test(c, t, Format.TLV);
		
	}
	
	
}
