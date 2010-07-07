package translators.sql.testing.ecologylabXmlTest;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.SIMPLTranslationException;
import ecologylab.xml.TranslationScope;

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
		c.serialize(buffy);
		System.out.println(buffy);
		System.out.println('\n');
		
		/*translate from xml to java*/
		ElementState c2	= RssTranslationsTest.get().deserializeCharSequence(buffy);
		
		System.out.println("retranslated by ElementState");
		System.out.println(c2.getClass().getCanonicalName());
		System.out.println(c2.getClass().getSuperclass().getCanonicalName()); 
		Class<?>[] thisClasses = c2.getClass().getClasses();
		for (Class<?> class1 : thisClasses)
		{
			System.out.println(class1.getCanonicalName()); 
		}
		c2.serialize(System.out);
//			println(c.translateToXML());

	}

}
