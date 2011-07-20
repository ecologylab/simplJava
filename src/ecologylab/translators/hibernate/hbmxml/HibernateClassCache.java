/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;

/**
 * The Hibernate mapping of class cache configurations.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
public class HibernateClassCache extends ElementState
{

	public static final String	TRANSACTIONAL					= "transactional";

	public static final String	READ_WRITE						= "read-write";

	public static final String	NONSTRICT_READ_WRITE	= "nonstrict-read-write";

	public static final String	READ_ONLY							= "read-only";

	@simpl_scalar
	private String							usage									= READ_ONLY;

	public HibernateClassCache()
	{
	}

	public HibernateClassCache(String usage)
	{
		this.usage = usage;
	}

	public void setUsage(String usage)
	{
		this.usage = usage;
	}

	public String getUsage()
	{
		return usage;
	}

}
