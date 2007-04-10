/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.io.File;

import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.generic.Generic;
import ecologylab.xml.xml_inherit;

/**
 * Pref for a Float
 * 
 * @author ross
 * 
 */
@xml_inherit public class PrefFile extends Pref<File>
{
    /**
     * Value of Pref
     */
    @xml_attribute String   value;

    @xml_attribute int      relativePath;

    public static final int CODE_BASE    = 1;

    public static final int APP_DATA_DIR = 2;

    File                    fileValue    = null;

    /** No-argument constructor for XML translation. */
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

    @Override public void setValue(File newValue)
    {
        this.value = newValue.getAbsolutePath();
    }

    File file()
    {

        if (fileValue == null)
        {
            switch (relativePath)
            {
            case (CODE_BASE):
                this.fileValue = new File(Generic.codeBase().file(), value);
                break;
            case (APP_DATA_DIR):
                this.fileValue = new File(PropertiesAndDirectories.applicationDataDir(), value);
            break;
            default:
                this.fileValue = new File(value);
            }
        }

        return fileValue;
    }
}
