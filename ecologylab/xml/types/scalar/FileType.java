/*
 * Created on Dec 22, 2006
 */
package ecologylab.xml.types.scalar;

import java.io.File;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class FileType extends ScalarType<File>
{

    /**
     */
    public FileType()
    {
        super(File.class);
    }

    @Override public File getInstance(String value)
    {
        return new File(value);
    }
}
