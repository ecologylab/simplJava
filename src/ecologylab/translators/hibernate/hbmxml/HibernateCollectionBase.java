package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.simpl_inherit;

@simpl_inherit
public abstract class HibernateCollectionBase extends HibernateFieldBase
{

	@simpl_scalar
	private String										table;

	@simpl_scalar
	private boolean										inverse;

	/**
	 * some usable values: all|none|save-update|delete|all-delete-orphan|delete-orphan
	 */
	@simpl_scalar
	private boolean										cascade;

	@simpl_composite
	private HibernateKey							key;

	@simpl_composite
	@simpl_classes({HibernateElement.class, HibernateManyToMany.class})
	@simpl_serialization_order(10)
	private HibernateAssociationBase	association;

	public HibernateCollectionBase()
	{
		super();
		this.setLazy(LAZY_TRUE);
	}

	public String getTable()
	{
		return table;
	}

	public void setTable(String table)
	{
		this.table = table;
	}

	public boolean isInverse()
	{
		return inverse;
	}

	public void setInverse(boolean inverse)
	{
		this.inverse = inverse;
	}

	public boolean isCascade()
	{
		return cascade;
	}

	public void setCascade(boolean cascade)
	{
		this.cascade = cascade;
	}

	public HibernateKey getKey()
	{
		return key;
	}

	public void setKey(HibernateKey key)
	{
		this.key = key;
	}

	public HibernateAssociationBase getAssociation()
	{
		return association;
	}

	public void setAssociation(HibernateAssociationBase assocation)
	{
		this.association = assocation;
	}

}