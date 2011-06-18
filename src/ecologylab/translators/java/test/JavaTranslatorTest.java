package ecologylab.translators.java.test;

import java.io.File;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.library.rss.Channel;
import ecologylab.serialization.library.rss.Item;
import ecologylab.serialization.library.rss.RssState;
import ecologylab.standalone.xmlpolymorph.BItem;
import ecologylab.standalone.xmlpolymorph.SchmItem;
import ecologylab.standalone.xmlpolymorph.Schmannel;
import ecologylab.translators.java.JavaTranslator;

public class JavaTranslatorTest {
	
	public static void main(String[] args) 
	{
		try{
			testSerialization(args[0]);
			
			//System.out.println("testing deserialization");
			//testDeserialization(args[0]);
			//testJavaCodeGeneration(args[0],args[1]);
			
		}catch(Exception ex)
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
		TranslationScope ts2 = TranslationScope.get("RSSTranslations5", RssState.class, Item.class, Channel.class);
		TranslationScope.setGraphSwitch();
		ts2.serialize(new File(filename));
	}
	
	/**
	 * Testing deserialising the translation scope
	 * 
	 * @param filename
	 * @throws Exception
	 */
	private static void testDeserialization(String filename) throws Exception
	{
		TranslationScope ts = TranslationScope.get("tscope_tscope2", TranslationScope.class, ClassDescriptor.class, FieldDescriptor.class);
		TranslationScope.setGraphSwitch();
		TranslationScope t = (TranslationScope)ts.deserialize(filename);
	}	
	
	/**
	 * Testing the Java code generation from serialised translation scope
	 * 
	 * @param filename
	 * @throws Exception
	 */
	private static void testJavaCodeGeneration(String filename, String codeLocation) throws Exception
	{
		JavaTranslator c = new JavaTranslator();
		TranslationScope ts = TranslationScope.get("tscope_tscope2", TranslationScope.class, ClassDescriptor.class, FieldDescriptor.class);
		TranslationScope.setGraphSwitch();
		TranslationScope t = (TranslationScope)ts.deserialize(filename);
		
		TranslationScope.AddTranslationScope(t.getName(),t);
		c.translateToJava(new File(codeLocation),t);
	}	

}
