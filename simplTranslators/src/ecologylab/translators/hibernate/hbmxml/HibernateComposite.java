/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * The Hibernate mapping for many-to-one relationships (or composite properties).
 * 
 * @author quyin
 * 
 */
@simpl_inherit
@simpl_tag("many-to-one")
public class HibernateComposite extends HibernateProperty
{

	@simpl_scalar
	@simpl_tag("class")
	private String	compositeClassName;

	public HibernateComposite()
	{
		super();
		this.setLazy(LAZY_PROXY);
	}

	public void setCompositeClassName(String compositeClassName)
	{
		this.compositeClassName = compositeClassName;
	}

	public String getCompositeClassName()
	{
		return compositeClassName;
	}

}
