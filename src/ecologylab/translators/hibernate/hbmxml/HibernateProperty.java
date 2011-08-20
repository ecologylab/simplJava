package ecologylab.translators.hibernate.hbmxml;

import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

/**
 * The Hibernate mapping of properties.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
@xml_tag("property")
public class HibernateProperty extends HibernateFieldBase
{

	@simpl_scalar
	private String	access;

	@simpl_scalar
	private String	type;

	@simpl_scalar
	private String	column;
	
	@simpl_scalar
	private int			length;

	@simpl_scalar
	@xml_tag("not-null")
	private boolean	notNull;

	@simpl_scalar
	private boolean	unique;

	@simpl_scalar
	private String	index;

	@simpl_scalar
	private boolean	update;

	@simpl_scalar
	private boolean	insert;

	public HibernateProperty()
	{
		super();
		this.setLazy(LAZY_FALSE);
	}

	public String getAccess()
	{
		return access;
	}

	public void setAccess(String access)
	{
		this.access = access;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
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

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public boolean isNotNull()
	{
		return notNull;
	}

	public void setNotNull(boolean notNull)
	{
		this.notNull = notNull;
	}

	public boolean isUnique()
	{
		return unique;
	}

	public void setUnique(boolean unique)
	{
		this.unique = unique;
	}

	public String getIndex()
	{
		return index;
	}

	public void setIndex(String index)
	{
		this.index = index;
	}

	public boolean isUpdate()
	{
		return update;
	}

	public void setUpdate(boolean update)
	{
		this.update = update;
	}

	public boolean isInsert()
	{
		return insert;
	}

	public void setInsert(boolean insert)
	{
		this.insert = insert;
	}

}
