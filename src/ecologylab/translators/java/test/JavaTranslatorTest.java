package ecologylab.translators.java.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.generic.Debug;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.DescriptorBase;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.Format;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.library.rss.Channel;
import ecologylab.serialization.library.rss.Item;
import ecologylab.serialization.library.rss.RssState;
import ecologylab.standalone.xmlpolymorph.BItem;
import ecologylab.standalone.xmlpolymorph.SchmItem;
import ecologylab.standalone.xmlpolymorph.Schmannel;
import ecologylab.translators.java.JavaTranslationException;
import ecologylab.translators.java.JavaTranslator;
import ecologylab.translators.parser.JavaDocParser;

public class JavaTranslatorTest 
extends Debug
{
	public static final String	OUTPUT_DIR	= PropertiesAndDirectories.isWindows() ? "c:/tmp/" : "/tmp/";

	public static void main(String[] args)
	{
		try
		{
			testJavaCodeGeneration(OUTPUT_DIR + "serialize.xml", OUTPUT_DIR + "test/");
			testSerializationWithPolymorph(args[0]);
			testJavaCodeGenerationWithPolymorphicInfo(OUTPUT_DIR + "polySerialize.xml", OUTPUT_DIR +"testpoly/");
			testJavaCodeGenerationWithInheritance(OUTPUT_DIR);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Testing serialising Translation scope
	 * 
	 * @param filename
	 * @throws Exception
	 */
	private static void testSerialization(String filename) throws Exception
	{
		println("testSerialization("+filename);
		DescriptorBase.setJavaParser(new JavaDocParser());
		TranslationScope ts2 = TranslationScope.get("RSSTranslations5", RssState.class, Item.class,
				Channel.class);
		TranslationScope.enableGraphSerialization();
		ClassDescriptor.serialize(ts2, new File(filename), Format.XML);
		
	}

	/**
	 * Testing serialising Translation scope
	 * 
	 * @param filename
	 * @throws Exception
	 */
	private static void testSerializationWithPolymorph(String filename) throws Exception
	{
		println("testSerializationWithPolymorph("+filename);
		DescriptorBase.setJavaParser(new JavaDocParser());
		TranslationScope ts2 = TranslationScope.get("RSSTranslations", Schmannel.class, BItem.class,
				SchmItem.class, RssState.class, Item.class, Channel.class);
		TranslationScope.enableGraphSerialization();
		ClassDescriptor.serialize(ts2, new File(filename), Format.XML);
	}

	/**
	 * Testing deserialising the translation scope
	 * 
	 * @param filename
	 * @throws Exception
	 */
	private static void testDeserialization(String filename) throws Exception
	{
		println("testDeserialization("+filename);
		TranslationScope ts = TranslationScope.get("tscope_tscope2", TranslationScope.class,
				ClassDescriptor.class, FieldDescriptor.class);
		TranslationScope.enableGraphSerialization();
		TranslationScope t = (TranslationScope) ts.deserialize(new File(filename), Format.XML);
		System.out.println("Test");
	}

	/**
	 * Testing the Java code generation from serialised translation scope
	 * 
	 * @param filename
	 * @throws Exception
	 */
	private static void testJavaCodeGeneration(String filename, String codeLocation) throws Exception
	{
		println("testJavaCodeGeneration"+filename);
		testSerialization(filename);
		JavaTranslator c = new JavaTranslator();
		TranslationScope ts = TranslationScope.get("tscope_tscope2", TranslationScope.class,
				ClassDescriptor.class, FieldDescriptor.class);
		TranslationScope.enableGraphSerialization();
		TranslationScope t = (TranslationScope) ts.deserialize(new File(filename), Format.XML);

		c.translateToJava(new File(codeLocation), t);
	}

	/**
	 * Testing the Java code generation from serialised translation scope
	 * 
	 * @param filename
	 * @throws Exception
	 */
	private static void testJavaCodeGenerationWithPolymorphicInfo(String filename, String codeLocation)
			throws Exception
	{
		testSerializationWithPolymorph(filename);
		println("testJavaCodeGenerationWithPolymorphicInfo("+filename+", "+ codeLocation);
		JavaTranslator c = new JavaTranslator();
		TranslationScope ts = TranslationScope.get("tscope_tscope_polymorphic", TranslationScope.class,
				ClassDescriptor.class, FieldDescriptor.class);
		TranslationScope.enableGraphSerialization();
		TranslationScope t = (TranslationScope) ts.deserialize(new File(filename), Format.XML);

		c.translateToJava(new File(codeLocation), t);
	}

	@simpl_inherit
	class Base extends ElementState
	{
		@simpl_scalar
		private String	fieldA;
	}

	@simpl_inherit
	class Sub extends Base
	{
		@simpl_scalar
		private String						fieldB;

		@simpl_scalar
		private int								fieldC;

		@simpl_collection("item")
		private ArrayList<String>	strs;
	}

	private static void testJavaCodeGenerationWithInheritance(String outputDir) throws IOException,
			SIMPLTranslationException, JavaTranslationException
	{
		println("testJavaCodeGenerationWithInheritance(" + outputDir);
		TranslationScope ts = TranslationScope.get("test_inheritance", Base.class, Sub.class);
		TranslationScope.enableGraphSerialization();
		
		ClassDescriptor.serialize(ts, new File(outputDir + "ts.xml"), Format.XML);
		
		JavaTranslator jt = new JavaTranslator();
		jt.translateToJava(new File(outputDir + "jt"), ts);
	}

}
