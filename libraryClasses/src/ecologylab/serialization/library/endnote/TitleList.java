/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_scalar;
import simpl.types.element.StringState;
import ecologylab.serialization.ElementState;

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
