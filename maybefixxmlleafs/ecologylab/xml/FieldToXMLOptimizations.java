/**
 * 
 */
package ecologylab.xml;

import java.lang.reflect.Field;

/**
 * Small data structure used to optimize translation to XML.
 * 
 * Uses Class or Field so that it can dynamically get custom tags.
 * 
 * @author andruid
 */
class TagMapEntry
{
    public final String startOpenTag;

    public final String closeTag;
    
    public final String tagName;

    private TagMapEntry(String tagName)
    {
        startOpenTag = "<" + tagName;
        closeTag = "</" + tagName + ">";
        this.tagName = tagName;
    }

    TagMapEntry(Class<? extends ElementState> classObj, boolean compression)
    {
        this(classObj.isAnnotationPresent(ElementState.xml_tag.class) ? classObj.getAnnotation(
                ElementState.xml_tag.class).value() : XmlTools.getXmlTagName(classObj, "State", compression));
    }

    TagMapEntry(Field field, boolean compression)
    {
        this(field.isAnnotationPresent(ElementState.xml_tag.class) ? field.getAnnotation(
                ElementState.xml_tag.class).value() : XmlTools.getXmlTagName(field.getName(), null, compression));
    }

    @Override public String toString()
    {
        return "TagMapEntry" + closeTag;
    }
}