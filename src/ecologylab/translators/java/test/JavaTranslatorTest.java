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

public class JavaTranslatorTest {
	
	public static void main(String[] args) 
	{
		try{
			testSerialization(args[0]);
			
			System.out.println("testing deserialization");
			testDeserialization(args[0]);
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private static void testSerialization(String filename) throws Exception
	{
		TranslationScope ts2 = TranslationScope.get("RSSTranslations5", Schmannel.class, BItem.class, SchmItem.class,
				RssState.class, Item.class, Channel.class);
		ts2.serialize(new File(filename));
	}
	
	private static void testDeserialization(String filename) throws Exception
	{
		TranslationScope ts = TranslationScope.get("tscope_tscope2", TranslationScope.class, ClassDescriptor.class, FieldDescriptor.class);
		TranslationScope t = (TranslationScope)ts.deserialize(filename);
	}	

}
