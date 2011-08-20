/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

/**
 * The Hibernate mapping of many-to-many relationships (used for element collections).
 * 
 * @author quyin
 * 
 */
@simpl_inherit
@xml_tag("list")
public class HibernateList extends HibernateCollectionBase
{

	@simpl_composite
	private HibernateIndex	index;

	public HibernateList()
	{
		super();
		this.setLazy(LAZY_TRUE);
	}

	public HibernateIndex getIndex()
	{
		return index;
	}

	public void setIndex(HibernateIndex index)
	{
		this.index = index;
	}

}
