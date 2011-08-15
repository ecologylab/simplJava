/**
 * 
 */
package ecologylab.standalone;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.library.jnlp.information.AssociationElement;
/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public @simpl_tag("doobie-doobie-dah_dooooooo") class TestXMLTag extends ElementState
{
    @simpl_scalar @simpl_tag("as-df") String asdf;
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
