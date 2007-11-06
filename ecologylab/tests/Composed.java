/**
 * 
 */
package ecologylab.tests;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.ElementState.xml_tag;
import ecologylab.xml.types.element.ArrayListState;

/**
 * @author toupsz
 *
 */
@xml_tag("fred:flintstone") 
public class Composed extends ElementState
{
    @xml_nested ArrayListState<ClassTagged> tagged = new ArrayListState<ClassTagged>();
    
	@xml_attribute int	x = 22;
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

    public static void main(String[] args) throws XmlTranslationException
    {
        TranslationSpace ts = TranslationSpace.get("testXMLTag", classes);
        
        Composed c = new Composed();
        
        final String translatedXML = c.translateToXML();
        
		System.out.println(translatedXML);
        
        Composed retranslated	= (Composed) ElementState.translateFromXMLString(translatedXML, ts);
//        Composed retranslated	= (Composed) ElementState.translateFromXMLSAX(translatedXML, ts);
        
        c.translateToXML(System.out);
        System.out.println("\n\nretranslated:");
        
        retranslated.writePrettyXML(System.out);
//        retranslated.translateToXML(System.out);
    }
}
