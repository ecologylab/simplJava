package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.simpl_inherit;

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
