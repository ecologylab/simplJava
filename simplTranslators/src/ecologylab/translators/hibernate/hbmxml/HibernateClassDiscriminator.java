/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * The Hibernate mapping of class discriminators.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
public class HibernateClassDiscriminator extends HibernateBasic
{

	@simpl_scalar
	private String	column;

	@simpl_scalar
	private String	type	= "string";

	@simpl_scalar
	private boolean	force	= true;

	public HibernateClassDiscriminator()
	{
		super();
	}

	public HibernateClassDiscriminator(String column)
	{
		this();
		this.column = column;
	}

	public String getColumn()
	{
		return column;
	}

	public void setColumn(String column)
	{
		this.column = column;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setForce(boolean force)
	{
		this.force = force;
	}

	public boolean isForce()
	{
		return force;
	}

}
