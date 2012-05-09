package ecologylab.serialization.types;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_other_tags;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * Common base class for S.IM.PL scalar (ScalarType), composite (ClassDescriptor), and collection (CollectionType) types.
 * Also for FieldDescriptor.
 * <p/>
 * Fundamental unit of the S.IM.PL cross-language type system.
 *
 * @author andruid
 */
@simpl_inherit
abstract public class SimplBaseType extends ElementState
{

	public static final String	JAVA_PRIMITIVE_PACKAGE_NAME	= "java.lang";

	public static final String	CSHARP_PRIMITIVE_NAMESPACE	= "System";

	/**
	 * This is the unique platform-independent identifier that S.IM.PL uses to describe this entity.
	 */
	@simpl_scalar
	@simpl_other_tags({"field_name", "described_class_name"})
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

	public abstract String getJavaTypeName();

	public abstract String getCSharpTypeName();
	
	public abstract String getCSharpNamespace();

	public abstract String getObjectiveCTypeName();

	public abstract String getDbTypeName();
	
}