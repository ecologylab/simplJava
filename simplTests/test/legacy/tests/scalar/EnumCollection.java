package legacy.tests.scalar;

import java.util.ArrayList;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.FieldUsage;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.formatenums.Format;

public class EnumCollection implements TestCase{
	
	@simpl_collection("enums")
	ArrayList<Enum>	collectionOfEnums;
	
	public EnumCollection()
	{
		
	}
	
	public EnumCollection(ArrayList<Enum> ce) {
		collectionOfEnums = ce;
	}

	@Override
	public void runTest() throws SIMPLTranslationException {

		ArrayList<Enum> ce = new ArrayList<Enum>();
		ce.add(new Enum(NonsenseEnum.BUCKLE_MY_SHOES));
		ce.add(new Enum(NonsenseEnum.TWO));
		ce.add(new Enum(NonsenseEnum.ONE));
		
		EnumCollection c = new EnumCollection(ce);
		
		SimplTypesScope t = SimplTypesScope.get("enumCollectionTest",EnumCollection.class);		
		SimplTypesScope.enableGraphSerialization();

		TestingUtils.test(c, t, Format.XML);
		TestingUtils.test(c, t, Format.JSON);
		TestingUtils.test(c, t, Format.TLV);
		
	}
	
	
}
