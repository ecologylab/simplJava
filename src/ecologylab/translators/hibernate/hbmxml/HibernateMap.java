package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_tag;

@simpl_inherit
@simpl_tag("map")
public class HibernateMap extends HibernateCollectionBase
{

	@simpl_composite
	@simpl_tag("map-key")
	private HibernateMapKey	mapKey;

	public HibernateMap()
	{
		super();
		this.setLazy(LAZY_TRUE);
	}

	public HibernateMapKey getMapKey()
	{
		return mapKey;
	}

	public void setMapKey(HibernateMapKey mapKey)
	{
		this.mapKey = mapKey;
	}

}
