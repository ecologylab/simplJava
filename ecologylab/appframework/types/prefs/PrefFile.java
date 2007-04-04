/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.io.File;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;

/**
 * Pref for a Float
 * 
 * @author ross
 *
 */

@xml_inherit
public class PrefFile extends Pref<File>
{
    /**
     * Value of Pref
     */
    @xml_attribute File            value;
    
    /**
     * 
     */
    public PrefFile()
    {
        super();
    }
    /**
     * Instantiate Pref to value
     * 
     * @param value
     */
    public PrefFile(File value)
    {
        super();
        this.value  = value;
    }

    /**
     * Get the value of the Pref
     * 
     * @return  The value of the Pref
     */
    @Override
    File getValue()
    {
        return value;
    }
    
    @Override
    public void setValue(File newValue)
    {
        // TODO Auto-generated method stub
        this.value  = newValue;
    }
}
