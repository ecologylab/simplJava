/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.library.endnote;

import ecologylab.xml.ElementState;
import ecologylab.xml.subelements.StringState;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public class TitleList extends ElementState
{
    private @xml_leaf String title;
    
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
    public String getTitle()
    {
        return title;
    }

}
