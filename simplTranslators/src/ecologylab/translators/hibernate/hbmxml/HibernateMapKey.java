package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

@simpl_inherit
@simpl_tag("map-key")
public class HibernateMapKey extends HibernateBasic
{

	@simpl_scalar
	private String	column;

	@simpl_scalar
	private String	type;

	@simpl_scalar
	private int			length;
	
	public HibernateMapKey()
	{
		super();
	}
	
	public HibernateMapKey(String type)
	{
		this();
		this.type = type;
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

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

}
