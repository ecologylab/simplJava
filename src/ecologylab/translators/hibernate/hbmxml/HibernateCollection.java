/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.ElementState.xml_tag;

/**
 * The Hibernate mapping of many-to-many relationships (used for element collections).
 * 
 * @author quyin
 * 
 */
@simpl_inherit
@xml_tag("list")
public class HibernateCollection extends HibernateFieldBase
{

	@simpl_scalar
	private String										table;

	@simpl_scalar
	private boolean										inverse;

	@simpl_composite
	private HibernateKey							key;
	
	@simpl_composite
	private HibernateIndex						index;

	@simpl_composite
	@xml_tag("many-to-many")
	private HibernateManyToMany				manyToMany;

	@simpl_composite
	private HibernateCollectionScalar	element;

	public HibernateCollection()
	{
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

	public void setInverse(boolean inverse)
	{
		this.inverse = inverse;
	}

	public boolean isInverse()
	{
		return inverse;
	}

	public HibernateKey getKey()
	{
		return key;
	}

	public void setKey(HibernateKey key)
	{
		this.key = key;
	}

	public HibernateIndex getIndex()
	{
		return index;
	}

	public void setIndex(HibernateIndex index)
	{
		this.index = index;
	}

	public void setManyToMany(HibernateManyToMany manyToMany)
	{
		this.manyToMany = manyToMany;
	}

	public HibernateManyToMany getManyToMany()
	{
		return manyToMany;
	}

	public HibernateCollectionScalar getElement()
	{
		return element;
	}

	public void setElement(HibernateCollectionScalar element)
	{
		this.element = element;
	}

}
