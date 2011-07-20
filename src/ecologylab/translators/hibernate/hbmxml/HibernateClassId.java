/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;

/**
 * The Hibernate mapping of class IDs.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
public class HibernateClassId extends ElementState
{

	@simpl_scalar
	private String										name;

	@simpl_scalar
	private String										column;

	@simpl_composite
	private HibernateClassIdGenerator	generator;

	public HibernateClassId()
	{
	}

	public HibernateClassId(String name, String column, HibernateClassIdGenerator generator)
	{
		this.name = name;
		this.column = column;
		this.generator = generator;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getColumn()
	{
		return column;
	}

	public void setColumn(String column)
	{
		this.column = column;
	}

	public HibernateClassIdGenerator getGenerator()
	{
		return generator;
	}

	public void setGenerator(HibernateClassIdGenerator generator)
	{
		this.generator = generator;
	}

}
