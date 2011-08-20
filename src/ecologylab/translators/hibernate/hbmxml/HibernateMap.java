package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

@simpl_inherit
@xml_tag("map")
public class HibernateMap extends HibernateCollectionBase
{

	@simpl_composite
	@xml_tag("map-key")
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
