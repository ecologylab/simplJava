/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import java.io.File;

import simpl.core.SimplTypesScope;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;
import simpl.formats.enums.StringFormat;


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
