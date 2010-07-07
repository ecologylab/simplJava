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
            XmlState fromFile = (XmlState) TranslationScope.translateFromXML("/Users/toupsz/Desktop/RSBib.xml", EndnoteNameSpace.get());
            
            System.out.println(fromFile.serialize());
        }
        catch (SIMPLTranslationException e)
        {
            e.printStackTrace();
        }
    }

}
