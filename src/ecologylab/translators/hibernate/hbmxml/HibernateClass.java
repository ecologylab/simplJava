package ecologylab.translators.hibernate.hbmxml;

import ecologylab.generic.HashMapArrayList;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.types.element.Mappable;
import ecologylab.serialization.simpl_inherit;

/**
 * The Hibernate mapping of a class (or entity).
 * 
 * @author quyin
 * 
 */
@simpl_inherit
@xml_tag("class")
public class HibernateClass extends ElementState implements Mappable<String>
{

	@simpl_scalar
	private String																				name;

	@simpl_scalar
	private String																				table;

	@simpl_composite
	private HibernateClassCache														cache;

	@simpl_composite
	private HibernateClassId															id;

	@simpl_scalar
	@xml_tag("discriminator-value")
	private String																				discriminatorValue;

	@simpl_composite
	private HibernateClassDiscriminator										discriminator;

	@simpl_map
	@simpl_nowrap
	@simpl_classes({ HibernateFieldBase.class, HibernateProperty.class, HibernateComposite.class, HibernateCollection.class })
	private HashMapArrayList<String, HibernateFieldBase>	properties;

	public HibernateClass()
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

	public String getTable()
	{
		return table;
	}

	public void setTable(String table)
	{
		this.table = table;
	}

	public HibernateClassDiscriminator getDiscriminator()
	{
		return discriminator;
	}

	public void setDiscriminator(HibernateClassDiscriminator discriminator)
	{
		this.discriminator = discriminator;
	}

	public String getDiscriminatorValue()
	{
		return discriminatorValue;
	}

	public void setDiscriminatorValue(String discriminatorValue)
	{
		this.discriminatorValue = discriminatorValue;
	}

	public void setCache(HibernateClassCache cache)
	{
		this.cache = cache;
	}

	public HibernateClassCache getCache()
	{
		return cache;
	}

	public HibernateClassId getId()
	{
		return id;
	}

	public void setId(HibernateClassId id)
	{
		this.id = id;
	}

	public void setProperties(HashMapArrayList<String, HibernateFieldBase> properties)
	{
		this.properties = properties;
	}

	public HashMapArrayList<String, HibernateFieldBase> getProperties()
	{
		return properties;
	}

	public String key()
	{
		return name;
	}

	public boolean isColumnMapped(String associationTableColumnName)
	{
		if (properties != null)
		{
			for (HibernateFieldBase mappedField : properties)
			{
				if (mappedField instanceof HibernateCollection)
				{
					HibernateCollection mappedCollection = (HibernateCollection) mappedField;
					HibernateManyToMany m2m = mappedCollection.getManyToMany();
					if (m2m != null && associationTableColumnName.equals(m2m.getColumn()))
						return true;
				}
			}
		}
		return false;
	}

}
