/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import java.io.File;

import ecologylab.serialization.Format;
import ecologylab.serialization.SIMPLTranslationException;

public class TestEndnoteXML
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			XmlState fromFile = (XmlState) EndnoteNameSpace.get().deserialize(
					new File("/Users/toupsz/Desktop/RSBib.xml"), Format.XML);

			System.out.println(fromFile.serialize());
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}
	}

}
