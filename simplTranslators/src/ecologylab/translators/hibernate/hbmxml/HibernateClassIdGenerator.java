/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

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
	@simpl_tag("class")
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
