/**
 * 
 */
package ecologylab.xml.library.jnlp.information;

import ecologylab.xml.ElementState;
import ecologylab.xml.types.element.Mappable;

/**
 * A short statement about the application. Description elements are optional. The kind attribute defines how the
 * description should be used. It can have one of the following values:
 * 
 * <ul>
 * <li>one-line: If a reference to the application is going to appear on one row in a list or a table, this description
 * will be used.</li>
 * <li>short: If a reference to the application is going to be displayed in a situation where there is room for a
 * paragraph, this description is used.</li>
 * <li>tooltip: If a reference to the application is going to appear in a tooltip, this description is used.</li>
 * </ul>
 * 
 * Only one description element of each kind can be specified. A description element without a kind is used as a default
 * value. Thus, if Java Web Start needs a description of kind short, and it is not specified in the JNLP file, then the
 * text from the description without an attribute is used.
 * 
 * All descriptions contain plain text. No formatting, such as with HTML tags, is supported.
 * 
 * @author Zach
 */
public class Description extends ElementState implements Mappable<String>
{
    @xml_attribute private String kind;

    @xml_leaf private String      desc;

    /**
     * 
     */
    public Description()
    {
        super();
    }

    /**
     * @see ecologylab.xml.types.element.Mappable#key()
     */
    public String key()
    {
        return desc;
    }

    /**
     * @return the desc
     */
    public String getDesc()
    {
        return desc;
    }

}
