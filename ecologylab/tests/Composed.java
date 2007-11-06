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
@xml_tag("fred") 
public class Composed extends ElementState
{
    @xml_nested ArrayListState<ClassTagged> tagged = new ArrayListState<ClassTagged>();
    
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
        
        System.out.println(c.translateToXML());
        
         Composed composed	= (Composed) ElementState.translateFromXMLString(c.translateToXML(), ts);
        
        c.translateToXML(System.out);
        System.out.println("\n");
        
        composed.writePrettyXML(System.out);
    }
}
