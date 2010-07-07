/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.library.endnote;

import ecologylab.xml.ElementState;
import ecologylab.xml.Hint;
import ecologylab.xml.types.element.StringState;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class TitleList extends ElementState
{
    private @simpl_scalar @simpl_hints(Hint.XML_LEAF) String title;
    
    private @simpl_composite StringState secondaryTitle;
    
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
