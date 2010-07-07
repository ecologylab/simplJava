package ecologylab.standalone;

import java.io.File;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.library.rest.Fields;
import ecologylab.serialization.library.rest.RESTTranslationSpace;

public class TestNameSpace 
{

	/**
	 * Requires the XML file location as an argument.
	 * @param args  The XML file location to test
	 */
	public static void main(String[] args) 
	{
		if (args.length < 1)
		{
			System.err.println("Usage: TestNameSpace <xml filename>");
			System.exit(1);
		}
		
		File xmlFile 	= new File(args[0]);
		TranslationScope tSpace
						= RESTTranslationSpace.get(); 
		try 
		{
			Fields fields= (Fields) tSpace.deserialize(xmlFile);
			System.out.println("Fields: \n" + fields.toString());
		} catch (SIMPLTranslationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
