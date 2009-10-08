/*
 * Created on Dec 22, 2006
 */
package ecologylab.xml.types.scalar;

import java.io.File;

import ecologylab.xml.ScalarUnmarshallingContext;

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

    @Override public File getInstance(String value, String[] formatStrings, ScalarUnmarshallingContext scalarUnmarshallingContext)
    {
		   File fileContext	= (scalarUnmarshallingContext == null) ? null : scalarUnmarshallingContext.fileContext();
		   return (fileContext == null) ? new File(value) : new File(fileContext, value);
    }
}
