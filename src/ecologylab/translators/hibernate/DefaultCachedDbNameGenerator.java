package ecologylab.translators.hibernate;

import java.util.HashMap;
import java.util.Map;

import ecologylab.generic.Debug;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.XMLTools;

/**
 * default implementation of the DbNameGenerator, with cache.
 * 
 * @author quyin
 *
 */
@SuppressWarnings("rawtypes")
public class DefaultCachedDbNameGenerator extends Debug implements DbNameGenerator
{

	public static final String	ASSOCIATION_TABLE_SEP	= "__";

	private Map<Object, String>	cachedNames						= new HashMap<Object, String>();

	protected String createTableName(ClassDescriptor cd)
	{
		// TODO potentially, need to escape SQL keywords: use ` as quote?
		return cd.getTagName();
	}

	protected String createColumnName(FieldDescriptor fd)
	{
		return createColumnName(fd.getName());
	}

	protected String createColumnName(String fieldName)
	{
		// TODO potentially, need to escape SQL keywords: use ` as quote?
		return XMLTools.getXmlTagName(fieldName, null);
	}

	public String getTableName(ClassDescriptor cd)
	{
		if (cachedNames.containsKey(cd))
			return cachedNames.get(cd);
		String name = createTableName(cd);
		cachedNames.put(cd, name);
		return name;
	}

	public String getColumnName(FieldDescriptor fd)
	{
		if (cachedNames.containsKey(fd))
			return cachedNames.get(fd);
		String name = createColumnName(fd);
		cachedNames.put(fd, name);
		return name;
	}

	public String getColumnName(String fieldName)
	{
		return createColumnName(fieldName);
	}

	public String getAssociationTableName(ClassDescriptor cd, FieldDescriptor fd)
	{
		return getTableName(cd) + ASSOCIATION_TABLE_SEP + getColumnName(fd);
	}

	public String getAssociationTableColumnName(ClassDescriptor cd)
	{
		return getTableName(cd) + "_id";
	}
	
	public void clearCache()
	{
		cachedNames.clear();
	}

}
