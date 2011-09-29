package ecologylab.translators.net.test;

import java.io.File;

import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.generic.Debug;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.DescriptorBase;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.Format;
import ecologylab.serialization.TranslationScope;
import ecologylab.translators.net.DotNetTranslator;
import ecologylab.translators.parser.JavaDocParser;

public class DotNetTranslatorTest extends Debug
{
	public static final String	OUTPUT_DIR	= PropertiesAndDirectories.isWindows() ? "c:/tmp/" : "/Users/aaron/Documents/tmp3/";

	public static void main(String[] args)
	{
		try
		{
			testCSharpCodeGeneration(OUTPUT_DIR + "serialize.xml", OUTPUT_DIR + "test/");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static void testCSharpCodeGeneration(String filename, String codeLocation) throws Exception
	{
		println("testCSharpCodeGeneration " + filename);
		testSerialization(filename);
		DotNetTranslator c = new DotNetTranslator();
		TranslationScope ts = TranslationScope.get("tscope_tscope2", TranslationScope.class,
				ClassDescriptor.class, FieldDescriptor.class);
		TranslationScope.enableGraphSerialization();
		TranslationScope t = (TranslationScope) ts.deserialize(new File(filename), Format.XML);
		
		c.translateToCSharp(new File(codeLocation), t);
		
	}
	
	private static void testSerialization(String filename) throws Exception
	{
		println("testSerialization("+filename);
		DescriptorBase.setJavaParser(new JavaDocParser());
		TranslationScope ts2 = TranslationScope.get("DCTranslations234", TranslationScope.class, ClassDescriptor.class, FieldDescriptor.class);
		TranslationScope.enableGraphSerialization();
		ClassDescriptor.serialize(ts2, new File(filename), Format.XML);
		
	}
}
