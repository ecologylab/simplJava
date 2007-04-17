/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.io.File;

import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.xml.xml_inherit;

/**
 * Metadata about a File Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */

@xml_inherit
public class MetaPrefFile extends MetaPref<File>
{
    /**
     * Default value for this MetaPref
     */
    @xml_attribute  File      defaultValue;
    @xml_attribute  int       pathContext   = ABSOLUTE_PATH;
    
    /** Indicates that value is an absolute path. */
    public static final int ABSOLUTE_PATH = 0;

    /**
     * Indicates that value is a path relative to the codebase of the
     * application using this Pref.
     */
    public static final int CODE_BASE     = 1;

    /**
     * Indicates that value is a path relative to the data directory associated
     * with the application using this Pref.
     */
    public static final int APP_DATA_DIR  = 2;
    
    /**
     * Instantiate.
     */
    public MetaPrefFile()
    {
        super();
    }
    
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref
     */
    public File getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * Construct a new instance of the Pref that matches this.
     * Use this to fill-in the default value.
     * 
     * @return
     */
    protected @Override Pref<File> getPrefInstance()
    {
        return new PrefFile();
    }
    
    
/*
    public boolean isWithinRange(File newValue)
    {
        return (range == null) ? true :  range.isWithinRange(newValue);
    }
    */
}