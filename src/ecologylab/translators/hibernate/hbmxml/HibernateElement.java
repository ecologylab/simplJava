/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

/**
 * @author quyin
 * 
 */
@simpl_inherit
@xml_tag("element")
public class HibernateElement extends HibernateAssociationBase
{

	@simpl_scalar
	private String	type;

	@simpl_scalar
	private int			length;

	@simpl_scalar
	@xml_tag("not-null")
	private boolean	notNull	= false;

	@simpl_scalar
	private boolean	unique	= false;

	public HibernateElement()
	{
		super();
	}

	public HibernateElement(String column, String type)
	{
		this();
		this.setColumn(column);
		this.type = type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
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
