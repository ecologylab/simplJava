/**
 * 
 */
package ecologylab.tests;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
@xml_tag("fred:flintstone") 
public class Composed extends ElementState
{
    @simpl_collection("ClassTagged") ArrayList<ClassTagged> tagged = new ArrayList<ClassTagged>();
    
	@simpl_scalar int	x = 22;
    /**
     * 
     */
    public Composed()
    {
        tagged.add(new ClassTagged());
        tagged.add(new ClassTagged());
        tagged.add(new ClassTagged());
    }
    static final Class[] classes = {Composed.class, ClassTagged.class, FieldTagged.class};

    public static void main(String[] args) throws SIMPLTranslationException
    {
        TranslationScope ts = TranslationScope.get("testXMLTag", classes);
        
        Composed c = new Composed();
        
        final StringBuilder translatedXML = c.serialize();
        
		System.out.println(translatedXML);
        
        Composed retranslated	= (Composed) ts.deserializeCharSequence(translatedXML);
//        Composed retranslated	= (Composed) ElementState.translateFromXMLSAX(translatedXML, ts);
        
        c.serialize(System.out);
        System.out.println("\n\nretranslated:");
        
        retranslated.serialize(System.out);
//        retranslated.translateToXML(System.out);
    }
}
