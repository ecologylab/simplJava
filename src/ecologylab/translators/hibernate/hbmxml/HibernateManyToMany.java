/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;

/**
 * The Hibernate mapping of many-to-many relationships.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
public class HibernateManyToMany extends ElementState
{

	@simpl_scalar
	private String	column;

	@simpl_scalar
	@xml_tag("class")
	private String	mappedClassName;

	public HibernateManyToMany()
	{
	}

	public HibernateManyToMany(String column, String mappedClassName)
	{
		this.column = column;
		this.mappedClassName = mappedClassName;
	}

	public String getColumn()
	{
		return column;
	}

	public void setColumn(String column)
	{
		this.column = column;
	}

	public String getMappedClassName()
	{
		return mappedClassName;
	}

	public void setMappedClassName(String mappedClassName)
	{
		this.mappedClassName = mappedClassName;
	}

}
