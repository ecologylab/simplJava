package ecologylab.serialization.types;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementState.xml_other_tags;

/**
 * Common base class for S.IM.PL scalar (ScalarType), composite (ClassDescriptor), and collection (CollectionType) types.
 * Also for FieldDescriptor.
 * <p/>
 * Fundamental unit of the S.IM.PL cross-language type system.
 *
 * @author andruid
 */
public class SimplBaseType extends ElementState
{

	/**
	 * This is the unique platform-independent identifier that S.IM.PL uses to describe this entity.
	 */
	@simpl_scalar
	@xml_other_tags({"field_name", "described_class_name"})
	protected String	name;

	public SimplBaseType()
	{

	}

	public SimplBaseType(String name)
	{
		this();
		this.name		= name;
	}

	/**
	 * This is the cross-platform S.IM.PL name.
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}

}