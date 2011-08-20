/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.simpl_inherit;

/**
 * The Hibernate mapping of class ID generators.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
public class HibernateClassIdGenerator extends HibernateBasic
{

	public static final HibernateClassIdGenerator	identityGenerator	= new HibernateClassIdGenerator("identity");

	@simpl_scalar
	@xml_tag("class")
	private String																generatorClass;

	public HibernateClassIdGenerator()
	{
		super();
	}

	public HibernateClassIdGenerator(String generatorClass)
	{
		this();
		this.generatorClass = generatorClass;
	}

	public void setGeneratorClass(String generatorClass)
	{
		this.generatorClass = generatorClass;
	}

	public String getGeneratorClass()
	{
		return generatorClass;
	}

}
