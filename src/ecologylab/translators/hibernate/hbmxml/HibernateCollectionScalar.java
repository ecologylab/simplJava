/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;

/**
 * @author quyin
 * 
 */
@simpl_inherit
public class HibernateCollectionScalar extends ElementState
{

	@simpl_scalar
	private String	column;

	@simpl_scalar
	private String	type;

	@simpl_scalar
	@xml_tag("not-null")
	private boolean	notNull	= false;

	@simpl_scalar
	private boolean	unique	= false;

	public HibernateCollectionScalar()
	{
	}

	public HibernateCollectionScalar(String column, String type)
	{
		this.column = column;
		this.type = type;
	}

	public String getColumn()
	{
		return column;
	}

	public void setColumn(String column)
	{
		this.column = column;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public boolean isNotNull()
	{
		return notNull;
	}

	public void setNotNull(boolean notNull)
	{
		this.notNull = notNull;
	}

	public boolean isUnique()
	{
		return unique;
	}

	public void setUnique(boolean unique)
	{
		this.unique = unique;
	}

}
