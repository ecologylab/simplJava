/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import simpl.annotations.dbal.simpl_composite;
import simpl.types.element.StringState;
import ecologylab.serialization.ElementState;

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
