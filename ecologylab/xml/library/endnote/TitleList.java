/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.endnote;

import ecologylab.xml.ElementState;
import ecologylab.xml.StringState;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public class TitleList extends ElementState
{
    private @xml_nested StringState title;
    
    private @xml_nested StringState secondaryTitle;
    
    /**
     * 
     */
    public TitleList()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the secondary_title
     */
    public StringState getSecondaryTitle()
    {
        return secondaryTitle;
    }

    /**
     * @return the title
     */
    public StringState getTitle()
    {
        return title;
    }

}
