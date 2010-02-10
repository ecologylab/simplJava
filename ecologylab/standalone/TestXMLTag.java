/**
 * 
 */
package ecologylab.standalone;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.ElementState.xml_tag;
import ecologylab.xml.library.jnlp.information.AssociationElement;
import ecologylab.xml.types.element.ArrayListState;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public @xml_tag("doobie-doobie-dah_dooooooo") class TestXMLTag extends ElementState
{
    @xml_attribute @xml_tag("as-df") String asdf;
    @xml_nested @xml_tag("nested-tag") ArrayListState<AssociationElement> list = new ArrayListState<AssociationElement>();

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

    public static void main(String[] args) throws XMLTranslationException
    {
        TestXMLTag test = new TestXMLTag("asdfasdfasdfasdfasdfasdf");

        Class[] classes =
        { TestXMLTag.class, ArrayListState.class, AssociationElement.class };
        
        ArrayListState<TestXMLTag> taggies = new ArrayListState<TestXMLTag>();

        for (int i = 0; i < 4; i++)
        {
            taggies.add(new TestXMLTag("asdf"+i));
        }
        
        System.out.println(test.translateToXML());
        System.out.println(ElementState.translateFromXMLCharSequence(test.translateToXML(),
                TranslationScope.get("test", classes)).translateToXML());
        
        System.out.println(taggies.translateToXML());
        System.out.println(ElementState.translateFromXMLCharSequence(taggies.translateToXML(),
                TranslationScope.get("test", classes)).translateToXML());

    }
}
