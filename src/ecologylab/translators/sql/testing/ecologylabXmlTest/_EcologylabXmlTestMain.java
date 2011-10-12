package ecologylab.translators.sql.testing.ecologylabXmlTest;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.StringFormat;
import ecologylab.serialization.SimplTypesScope;

public class _EcologylabXmlTestMain
{
	public static void main(String[] args) throws SIMPLTranslationException
	{
		/*java class definition*/ 
		ChannelTest c	= new ChannelTest();
		
		/*Item1*/ 
		ItemTest i1		= new ItemTest();
		i1.author	= "zach";
		i1.title	= "it is called rogue!";
		i1.link		= ParsedURL.getAbsolute("http://ecologylab.cs.tamu.edu/rogue/");
		i1.description = "its a game";
		
		/*item2*/
		ItemTest i2 = new ItemTest();
		i2.author = "andruid";
		i2.title = "it is called cf!";
		i2.description	= "its a creativity support tool";
		
		/*adding to channel*/ 
		c.items		= new ArrayList<ItemTest>();
		c.items. add(i1);
		c.items.add(i2);
 
		/*translate to xml*/
		StringBuilder buffy	= new StringBuilder();
		
		SimplTypesScope.serialize(c, System.out, StringFormat.XML);
//		c.serialize(System.out);
		
		ChannelTest ct = new ChannelTest();
		SimplTypesScope ts = SimplTypesScope.get("this", ChannelTest.class);
		
		SimplTypesScope.serialize(ts, System.out, StringFormat.XML);
		
		
		
		
		
		
		/*translate from xml to java*/
		ElementState c2	= (ElementState) RssTranslationsTest.get().deserialize(buffy, StringFormat.XML);
		
//		System.out.println("retranslated by ElementState");
//		System.out.println(c2.getClass().getCanonicalName());
//		System.out.println(c2.getClass().getSuperclass().getCanonicalName()); 
		Class<?>[] thisClasses = c2.getClass().getClasses();
		for (Class<?> class1 : thisClasses)
		{
//			System.out.println(class1.getCanonicalName()); 
		}
		Appendable a; 
//		c2.serialize(System.out);
//			println(c.translateToXML());

	}

}
