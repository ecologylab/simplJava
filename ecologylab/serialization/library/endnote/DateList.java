/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.types.element.StringState;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class DateList extends ElementState
{
    private @simpl_composite StringState year;
    
//    private @xml_nested String pub-dates = "";
    
    /**
     * 
     */
    public DateList()
    {
        // TODO Auto-generated constructor stub
    }
}
