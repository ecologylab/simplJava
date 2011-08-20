package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

@simpl_inherit
@xml_tag("index")
public class HibernateIndex extends HibernateBasic
{

	@simpl_scalar
	private String	column;

	/**
	 * required for maps.
	 */
	@simpl_scalar
	private String	type;

	@simpl_scalar
	private String	length;
	
	public HibernateIndex()
	{
		super();
	}

	public HibernateIndex(String columnName)
	{
		this();
		this.column = columnName;
	}

	public HibernateIndex(String columnName, String typeName)
	{
		this();
		this.column = columnName;
		this.type = typeName;
	}

	public String getColumn()
	{
		return column;
	}

	public void setColumn(String column)
	{
		this.column = column;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getLength()
	{
		return length;
	}

	public void setLength(String length)
	{
		this.length = length;
	}

}
