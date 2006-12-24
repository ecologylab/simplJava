/*
 * Created on Dec 22, 2006
 */
package ecologylab.types;

import java.io.File;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public class FileType extends Type
{

    /**
     * @param className
     * @param isPrimitive
     */
    public FileType()
    {
        super(File.class);
    }

    /* (non-Javadoc)
     * @see ecologylab.types.Type#getInstance(java.lang.String)
     */
    @Override public Object getInstance(String value)
    {
        return new File(value);
    }
}
