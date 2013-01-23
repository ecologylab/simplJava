package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public abstract class HibernateAssociationBase extends HibernateBasic
{

	@simpl_scalar
	private String	column;

	public HibernateAssociationBase()
	{
		super();
	}

	public String getColumn()
	{
		return column;
	}

	public void setColumn(String column)
	{
		this.column = column;
	}

}
