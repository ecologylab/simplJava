/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.io.File;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.TranslationContext;
import simpl.types.ScalarType;
import simpl.types.TypeRegistry;


/**
 * Metadata about a File Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */

@simpl_inherit
public class MetaPrefFile extends MetaPref<File>
{
    /**
     * Default value for this MetaPref
     */
    @simpl_scalar  File      defaultValue;
    @simpl_scalar  int       pathContext   = ABSOLUTE_PATH;
    
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
    
	public static final ScalarType FILE_SCALAR_TYPE	= TypeRegistry.getScalarType(File.class);

    /**
     * Instantiate.
     */
    public MetaPrefFile()
    {
        super(FILE_SCALAR_TYPE);
    }
    
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref
     */
    @Override
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

    /**
     * Get max value; returns null for this type.
     */
    @Override
    public File getMaxValue()
    {
        return null;
    }

    /**
     * Get min value; returns null for this type.
     */
    @Override
    public File getMinValue()
    {
        return null;
    }
}