/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;

/**
 * The Hibernate mapping of class discriminators.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
public class HibernateClassDiscriminator extends ElementState
{

	@simpl_scalar
	private String	column;

	@simpl_scalar
	private String	type	= "string";

	@simpl_scalar
	private boolean	force	= true;

	public HibernateClassDiscriminator()
	{
	}

	public HibernateClassDiscriminator(String column)
	{
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
