/**
 * 
 */
package ecologylab.standalone;

import ecologylab.generic.Debug;
import ecologylab.xml.ElementState;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.library.jnlp.JnlpState;
import ecologylab.xml.library.jnlp.JnlpTranslations;

/**
 * @author Zach
 *
 */
public class JNLPTestApp
{

    /**
     * @param args
     * @throws XmlTranslationException 
     */
    public static void main(String[] args) throws XmlTranslationException
    {
        Debug.println("translate from...");
        JnlpState jnlp = (JnlpState) ElementState.translateFromXML("c:\\jnlptest.jnlp", JnlpTranslations.get());
        Debug.println("...done.");
        
        Debug.println("translate to...");
        jnlp.writePrettyXML("c:\\jnlp2.txt");
        Debug.println("...done.");
    }

}
