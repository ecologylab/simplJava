package ecologylab.translators.hibernate;

import ecologylab.generic.Debug;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.XMLTools;

public class DbNameGenerator extends Debug
{

	public String createTableName(ClassDescriptor cd)
	{
		// TODO potentially, need to escape SQL keywords: use ` as quote?
		return cd.getTagName();
	}

	public String createColumnName(FieldDescriptor fd)
	{
		return createColumnName(fd.getName());
	}
	
	public String createColumnName(String fieldName)
	{
		// TODO potentially, need to escape SQL keywords: use ` as quote?
		return XMLTools.getXmlTagName(fieldName, null);
	}

}
