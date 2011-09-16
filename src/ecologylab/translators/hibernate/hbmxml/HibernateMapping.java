package ecologylab.translators.hibernate.hbmxml;

import ecologylab.generic.HashMapArrayList;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_map;
import ecologylab.serialization.annotations.simpl_map_key_field;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * The Hibernate mapping XML (hbm.xml) root element.
 * 
 * @author quyin
 * 
 */
@simpl_tag("hibernate-mapping")
@simpl_inherit
public class HibernateMapping extends HibernateBasic
{

	@simpl_scalar
	@simpl_tag("package")
	private String																		mappingPackageName;

	@simpl_map
	@simpl_map_key_field("name")
	@simpl_nowrap
	@simpl_classes({ HibernateClass.class, HibernateJoinedSubclass.class })
	private HashMapArrayList<String, HibernateClass>	mappedClasses;

	public HibernateMapping()
	{
		super();
		mappedClasses = new HashMapArrayList<String, HibernateClass>();
	}

	public void setMappingPackageName(String packageName)
	{
		this.mappingPackageName = packageName;
	}

	public String getMappingPackageName()
	{
		return mappingPackageName;
	}

	public void setMappedClasses(HashMapArrayList<String, HibernateClass> mappedClasses)
	{
		this.mappedClasses = mappedClasses;
	}

	public HashMapArrayList<String, HibernateClass> getMappedClasses()
	{
		return mappedClasses;
	}

}
