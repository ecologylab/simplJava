/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.io.File;

import ecologylab.appframework.EnvironmentGeneric;
import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.xml.xml_inherit;

/**
 * Pref indicating a File. Stores a value that indicates either an absolute
 * path, or one relative to the code base or application data dir for the
 * application using the Pref.
 * 
 * @author ross
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
@xml_inherit public class PrefFile extends Pref<File>
{
    /** Path associated with this preference. */
    @xml_attribute String   value;

    /**
     * Context indicating the type of path specified by value. Possible values
     * are ABSOLUTE_PATH, CODE_BASE, or APP_DATA_DIR.
     */
    @xml_attribute int      pathContext   = ABSOLUTE_PATH;

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
     * The cached File object that goes with this Pref; lazilly evaluated.
     * fileValue should NEVER be directly referenced, it should only be accessed
     * through the file() method.
     */
    File                    fileValue     = null;

    /** No-argument constructor for XML translation. */
    public PrefFile()
    {
        super();
    }
    
    public PrefFile(String name, String value, int pathContext)
    {
    	this.name = name;
    	this.value = value;
    	this.pathContext = pathContext;
    }

    /**
     * Instantiate Pref to value
     * 
     * @param value
     */
    public PrefFile(File value)
    {
        super();
        this.setValue(value);
    }

    /**
     * Get the value of the Pref. If this's file path includes '$FIND_PATH',
     * then this builds a file based upon the pathname provided by the App
     * Framework.
     * 
     * @return The value of the Pref
     */
    @Override File getValue()
    {
        return file();
    }

    /**
     * Sets up this Pref object to be associated with newValue as an absolute
     * path.
     * 
     * @see ecologylab.appframework.types.prefs.Pref#setValue(java.lang.Object)
     */
    @Override public void setValue(File newValue)
    {
        this.value = newValue.getAbsolutePath();
        
        this.prefChanged();
    }

    /**
     * Sets up this Pref object to be associated with newValue as a path
     * indicated by pathContext.
     * 
     * @param newValue
     * @param pathContext
     */
    public void setValue(String newValue, int pathContext)
    {
        this.value = newValue;
        this.pathContext = pathContext;
        
        this.prefChanged();
    }

    private final File file()
    {
        if (fileValue == null)
        {
            switch (pathContext)
            {
                case (CODE_BASE):
                    this.fileValue = new File(EnvironmentGeneric.codeBase().file(), value);
                    break;
                case (APP_DATA_DIR):
                    this.fileValue = new File(PropertiesAndDirectories
                            .applicationDataDir(), value);
                    break;
                default:
                    this.fileValue = new File(value);
            }
        }

        return fileValue;
    }

		/**
		 * @see ecologylab.appframework.types.prefs.Pref#clone()
		 */
		@Override
		public Pref<File> clone()
		{
			return new PrefFile(this.name, this.value, this.pathContext);
		}
}
