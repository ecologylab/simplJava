/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.endnote;

import ecologylab.xml.ElementState;
import ecologylab.xml.XmlTranslationException;

public class TestEndnoteXML
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            XmlState fromFile = (XmlState) ElementState.translateFromXML("/Users/toupsz/Desktop/RSBib.xml", EndnoteNameSpace.get());
            
            System.out.println(fromFile.translateToXML(false));
        }
        catch (XmlTranslationException e)
        {
            e.printStackTrace();
        }
    }

}
