/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * The Hibernate mapping of subclasses.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
@simpl_tag("joined-subclass")
public class HibernateJoinedSubclass extends HibernateClass
{

	@simpl_scalar
	@simpl_tag("extends")
	private String				extendsAttribute;

	@simpl_composite
	private HibernateKey	key;

	public HibernateJoinedSubclass()
	{
		super();
	}

	public HibernateJoinedSubclass(String extendsAttribute, HibernateKey key)
	{
		this();
		this.extendsAttribute = extendsAttribute;
		this.key = key;
	}

	public String getExtendsAttribute()
	{
		return extendsAttribute;
	}

	public void setExtendsAttribute(String extendsAttribute)
	{
		this.extendsAttribute = extendsAttribute;
	}

	public HibernateKey getKey()
	{
		return key;
	}

	public void setKey(HibernateKey key)
	{
		this.key = key;
	}

}
