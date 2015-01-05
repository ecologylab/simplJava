/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import java.io.File;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.formatenums.StringFormat;

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

			SimplTypesScope.serialize(fromFile, System.out, StringFormat.XML);
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}
	}

}
