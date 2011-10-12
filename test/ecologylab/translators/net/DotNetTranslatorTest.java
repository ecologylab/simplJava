package ecologylab.translators.net;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import ecologylab.generic.Debug;
import ecologylab.io.Files;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.translators.AbstractCodeTranslator;
import ecologylab.translators.CodeTranslationException;
import ecologylab.translators.net.sub.TestSub;

public class DotNetTranslatorTest extends Debug
{
	public static final String	OUTPUT_DIR	= ".." + Files.sep + "testCSharpCodeGenerator";

	@Test
	public void testBasics() throws IOException, SIMPLTranslationException, CodeTranslationException
	{
		SimplTypesScope tScope = SimplTypesScope.get("TestCSCodeGenBasics", new Class[] { TestBase.class, TestSub.class });
		AbstractCodeTranslator t = new DotNetTranslator();
		t.translate(new File(OUTPUT_DIR + Files.sep + "Basics"), tScope, null);
	}
	
//	public static void main(String[] args)
//	{
//		try
//		{
//			testCSharpCodeGeneration(OUTPUT_DIR + "serialize.xml", OUTPUT_DIR + "test/");
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//		}
//	}
//
//	private static void testCSharpCodeGeneration(String filename, String codeLocation) throws Exception
//	{
//		println("testCSharpCodeGeneration " + filename);
//		testSerialization(filename);
//		DotNetTranslator c = new DotNetTranslator();
//		TranslationScope ts = TranslationScope.get("tscope_tscope2", TranslationScope.class,
//				ClassDescriptor.class, FieldDescriptor.class);
//		TranslationScope.enableGraphSerialization();
//		TranslationScope t = (TranslationScope) ts.deserialize(new File(filename), Format.XML);
//		
//		c.translate(new File(codeLocation), t);
//		
//	}
//	
//	private static void testSerialization(String filename) throws Exception
//	{
//		println("testSerialization("+filename);
//		DescriptorBase.setJavaParser(new JavaDocParser());
//		TranslationScope ts2 = TranslationScope.get("DCTranslations234", TranslationScope.class, ClassDescriptor.class, FieldDescriptor.class);
//		TranslationScope.enableGraphSerialization();
//		ClassDescriptor.serialize(ts2, new File(filename), Format.XML);
//		
//	}
	
}
