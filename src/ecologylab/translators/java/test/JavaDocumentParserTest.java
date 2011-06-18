package ecologylab.translators.java.test;

import java.util.HashMap;

import ecologylab.serialization.JavaDocumentParser;
import ecologylab.serialization.library.rss.Item;

public class JavaDocumentParserTest {

	public static void main(String[] args)
	{
		try{
			JavaDocumentParser parser = new JavaDocumentParser(Item.class);
			parser.Parse();
			
			System.out.println("Class Comment is : " + parser.getClassComment());
			
			System.out.println("Field Comments");
			HashMap<String, String> fieldComments = parser.getFieldComments();
			for(String key : fieldComments.keySet())
			{
				System.out.println(key + " : " + fieldComments.get(key));
			}
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
