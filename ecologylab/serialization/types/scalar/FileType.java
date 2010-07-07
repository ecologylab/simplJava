/*
 * Created on Dec 22, 2006
 */
package ecologylab.serialization.types.scalar;

import java.io.File;

import ecologylab.serialization.ScalarUnmarshallingContext;

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

		@Override
		public String getCSharptType()
		{
			return MappingConstants.DOTNET_FILE;
		}

		@Override
		public String getDbType()
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getObjectiveCType()
		{
			return MappingConstants.OBJC_FILE;
		}
}
