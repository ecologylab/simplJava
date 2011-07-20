package ecologylab.translators.hibernate.hbmxml;

import ecologylab.generic.HashMapArrayList;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.ElementState.xml_tag;

/**
 * The Hibernate mapping XML (hbm.xml) root element.
 * 
 * @author quyin
 * 
 */
@xml_tag("hibernate-mapping")
@simpl_inherit
public class HibernateMapping extends ElementState
{

	@simpl_scalar
	@xml_tag("package")
	private String																		mappingPackageName;

	@simpl_map
	@simpl_nowrap
	@simpl_classes({ HibernateClass.class, HibernateJoinedSubclass.class })
	private HashMapArrayList<String, HibernateClass>	mappedClasses;

	public HibernateMapping()
	{
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
