/**
 * 
 */
package ecologylab.standalone;

import java.util.ArrayList;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.SIMPLTranslationException;
import ecologylab.xml.ElementState.xml_tag;
import ecologylab.xml.library.jnlp.information.AssociationElement;
/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public @xml_tag("doobie-doobie-dah_dooooooo") class TestXMLTag extends ElementState
{
    @simpl_scalar @xml_tag("as-df") String asdf;
    @simpl_collection("nested-tag") ArrayList<AssociationElement> list = new ArrayList<AssociationElement>();

    public TestXMLTag()
    {
        super();
    }

    public TestXMLTag(String asdf)
    {
        this.asdf = asdf;
        
        for (int i = 0; i < 4; i++)
        {
            list.add(new AssociationElement());
        }
    }

    public static void main(String[] args) throws SIMPLTranslationException
    {
        TestXMLTag test = new TestXMLTag("asdfasdfasdfasdfasdfasdf");

        Class[] classes =
        { TestXMLTag.class, ArrayList.class, AssociationElement.class };
        
        ArrayList<TestXMLTag> taggies = new ArrayList<TestXMLTag>();

        for (int i = 0; i < 4; i++)
        {
            taggies.add(new TestXMLTag("asdf"+i));
        }
        
        System.out.println(test.serialize());
        TranslationScope translationScope = TranslationScope.get("test", classes);
				System.out.println(translationScope.deserializeCharSequence(test.serialize()).serialize());
        
//        System.out.println(taggies.translateToXML());
//        System.out.println(ElementState.translateFromXMLCharSequence(taggies.translateToXML(),
//                TranslationScope.get("test", classes)).translateToXML());

    }
}
