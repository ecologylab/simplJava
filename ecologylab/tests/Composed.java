/**
 * 
 */
package ecologylab.tests;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.types.element.ArrayListState;

/**
 * @author toupsz
 *
 */
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

    public static void main(String[] args) throws XmlTranslationException
    {
        Composed c = new Composed();
        
        System.out.println(c.translateToXML());
        
        Class[] classes = {Composed.class, ClassTagged.class, FieldTagged.class};
        TranslationSpace ts = TranslationSpace.get("testXMLTag", classes);
        
        System.out.println(((Composed) ElementState.translateFromXMLString(c.translateToXML(), ts)).translateToXML());
    }
}
