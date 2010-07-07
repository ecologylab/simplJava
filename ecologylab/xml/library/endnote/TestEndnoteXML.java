/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.library.endnote;

import ecologylab.xml.SIMPLTranslationException;
import ecologylab.xml.TranslationScope;

public class TestEndnoteXML
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            XmlState fromFile = (XmlState) EndnoteNameSpace.get().deserialize("/Users/toupsz/Desktop/RSBib.xml");
            
            System.out.println(fromFile.serialize());
        }
        catch (SIMPLTranslationException e)
        {
            e.printStackTrace();
        }
    }

}
