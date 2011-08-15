package ecologylab.translators.hibernate;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;

/**
 * generate names used by object-relational mapping generator.
 * 
 * @author quyin
 * 
 */
public interface DbNameGenerator
{

	public static final String	DEFAULT_ORM_ID_FIELD_NAME						= "ormId";

	// public static final String DISCRIMINATOR_COLUMN_NAME = "type_discrim";

	public static final String	SCALAR_COLLECTION_VALUE_COLUMN_NAME	= "value";

	public String getTableName(ClassDescriptor cd);

	public String getColumnName(FieldDescriptor fd);

	public String getColumnName(String fieldName);

	public String getAssociationTableName(ClassDescriptor cd, FieldDescriptor fd);

	public String getAssociationTableColumnName(ClassDescriptor cd);

	/**
	 * clear cache if any.
	 */
	public void clearCache();

}
