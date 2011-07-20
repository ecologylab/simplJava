/**
 * 
 */
package ecologylab.translators.hibernate.hbmxml;

import java.util.ArrayList;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.simpl_inherit;

/**
 * The Hibernate mapping of subclasses.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
@xml_tag("joined-subclass")
public class HibernateJoinedSubclass extends HibernateClass
{

	@simpl_scalar
	@xml_tag("extends")
	private String			extendsAttribute;

	@simpl_composite
	private HibernateKey	key;

	public HibernateJoinedSubclass()
	{
	}
	
	public HibernateJoinedSubclass(String extendsAttribute, HibernateKey key)
	{
		this.extendsAttribute = extendsAttribute;
		this.key = key;
	}

	public String getExtendsAttribute()
	{
		return extendsAttribute;
	}

	public void setExtendsAttribute(String extendsAttribute)
	{
		this.extendsAttribute = extendsAttribute;
	}

	public HibernateKey getKey()
	{
		return key;
	}

	public void setKey(HibernateKey key)
	{
		this.key = key;
	}

	@Override
	protected void serializationPreHook()
	{
		super.serializationPreHook();
		
		ClassDescriptor cd = classDescriptor();
		ArrayList<FieldDescriptor> fieldDescriptors = cd.elementFieldDescriptors();
		FieldDescriptor keyFd = null;
		for (FieldDescriptor fd : fieldDescriptors)
			if (fd.getName().equals("key"))
				keyFd = fd;
		if (keyFd != null)
		{
			fieldDescriptors.remove(keyFd);
			fieldDescriptors.add(0, keyFd);
		}
	}

}
