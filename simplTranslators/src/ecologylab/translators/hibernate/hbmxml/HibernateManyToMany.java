/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * The Hibernate mapping of many-to-many relationships.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
@simpl_tag("many-to-many")
public class HibernateManyToMany extends HibernateAssociationBase
{
	
	@simpl_scalar
	@simpl_tag("class")
	private String	mappedClassName;
	
	@simpl_scalar
	private String	lazy;

	public HibernateManyToMany()
	{
		super();
		this.setLazy(HibernateFieldBase.LAZY_PROXY);
	}

	public HibernateManyToMany(String column, String mappedClassName)
	{
		this();
		this.setColumn(column);
		this.mappedClassName = mappedClassName;
	}

	public String getMappedClassName()
	{
		return mappedClassName;
	}

	public void setMappedClassName(String mappedClassName)
	{
		this.mappedClassName = mappedClassName;
	}

	public String getLazy()
	{
		return lazy;
	}

	public void setLazy(String lazy)
	{
		this.lazy = lazy;
	}

}
