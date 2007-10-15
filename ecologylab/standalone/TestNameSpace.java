package ecologylab.standalone;

import java.io.File;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.library.rest.Fields;
import ecologylab.xml.library.rest.RESTTranslationSpace;

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
		TranslationSpace tSpace
						= RESTTranslationSpace.get(); 
		try 
		{
			Fields fields= (Fields) ElementState.translateFromXML(xmlFile, tSpace);
			System.out.println("Fields: \n" + fields.toString());
		} catch (XmlTranslationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
