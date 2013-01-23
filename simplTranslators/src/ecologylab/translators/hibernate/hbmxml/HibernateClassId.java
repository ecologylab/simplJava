/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * The Hibernate mapping of class IDs.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
public class HibernateClassId extends HibernateBasic
{

	@simpl_scalar
	private String										name;

	@simpl_scalar
	private String										column;

	@simpl_composite
	private HibernateClassIdGenerator	generator;

	public HibernateClassId()
	{
		super();
	}

	public HibernateClassId(String name, String column, HibernateClassIdGenerator generator)
	{
		this();
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
