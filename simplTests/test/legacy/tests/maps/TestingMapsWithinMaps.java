package legacy.tests.maps;

import simpl.core.SimplTypesScope;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;
import legacy.tests.TestCase;
import legacy.tests.TestingUtils;

public class TestingMapsWithinMaps implements TestCase
{
	public static TranslationS createObject()
	{
		TranslationS trans = new TranslationS();

		ClassDes cd1 = new ClassDes("cd1");

		cd1.fieldDescriptorsByTagName.put("fd1_cd1", new FieldDes("fd1_cd1"));
		cd1.fieldDescriptorsByTagName.put("fd2_cd1", new FieldDes("fd2_cd1"));
		cd1.fieldDescriptorsByTagName.put("fd3_cd1", new FieldDes("fd3_cd1"));

		ClassDes cd2 = new ClassDes("cd2");
		cd2.fieldDescriptorsByTagName.put("fd1_cd2", new FieldDes("fd1_cd2"));
		cd2.fieldDescriptorsByTagName.put("fd2_cd2", new FieldDes("fd2_cd2"));
		cd2.fieldDescriptorsByTagName.put("fd3_cd2", new FieldDes("fd3_cd2"));

		trans.entriesByTag.put("cd1", cd1);
		trans.entriesByTag.put("cd2", cd2);

		return trans;
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
//		TranslationScope.enableGraphSerialization();

		TranslationS test = createObject();
		SimplTypesScope tScope = SimplTypesScope.get("testingMapWithinMapsTScope", TranslationS.class, ClassDes.class,
				FieldDes.class);
		
		SimplTypesScope.enableGraphSerialization();
		TestingUtils.serializeSimplTypesScope(tScope, "testingMapWithinMapsTScope",  Format.JSON);
		
		TestingUtils.test(test, tScope, Format.XML);
		TestingUtils.test(test, tScope, Format.JSON);
		TestingUtils.test(test, tScope, Format.TLV);

//		TranslationScope.disableGraphSerialization();

	}
}
