/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.types.element.Mappable;

/**
 * The base class for mapping fields.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
public abstract class HibernateFieldBase extends ElementState implements Mappable<String>
{

	public static final String	LAZY_FALSE		= "false";

	public static final String	LAZY_TRUE			= "true";

	public static final String	LAZY_PROXY		= "proxy";

	public static final String	LAZY_NO_PROXT	= "no-proxy";

	@simpl_scalar
	private String							name;

	@simpl_scalar
	private String							lazy;

	public HibernateFieldBase()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setLazy(String lazy)
	{
		this.lazy = lazy;
	}

	public String getLazy()
	{
		return lazy;
	}

	public String key()
	{
		return name;
	}

}
