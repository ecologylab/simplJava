/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;

/**
 * The Hibernate mapping of foreign-key. Used in subclass joining or composite/collection mapping.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
public class HibernateKey extends ElementState
{

	@simpl_scalar
	private String	column;

	@simpl_scalar
	@xml_tag("not-null")
	private boolean	notNull	= true;

	public HibernateKey()
	{
	}

	public HibernateKey(String column)
	{
		this.column = column;
	}

	public void setColumn(String column)
	{
		this.column = column;
	}

	public String getColumn()
	{
		return column;
	}

	public void setNotNull(boolean notNull)
	{
		this.notNull = notNull;
	}

	public boolean isNotNull()
	{
		return notNull;
	}

}
