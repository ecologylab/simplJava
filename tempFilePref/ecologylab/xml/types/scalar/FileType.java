/*
 * Created on Dec 22, 2006
 */
package ecologylab.xml.types.scalar;

import java.io.File;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public class FileType extends ScalarType
{

    /**
     */
    public FileType()
    {
        super(File.class);
    }

    @Override public Object getInstance(String value)
    {
        return new File(value);
    }
}
