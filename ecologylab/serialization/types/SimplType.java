/**
 * 
 */
package ecologylab.serialization.types;

import ecologylab.serialization.ElementState.simpl_scalar;

/**
 * Re-usable unit of the S.IM.PL type system.
 * 
 * This is the base class for ScalarType and CollectionType.
 * 
 * @author andruid
 */
public class SimplType extends SimplBaseType
{
	/**
	 * Short name of the type: without package.
	 */
	@simpl_scalar
	String											simpleName;

	/**
	 * Fully qualified name of the Java type that this represents, including package.
	 */
	@simpl_scalar
	private String							javaTypeName;
	
	/**
	 * Name for declaring the type in C#.
	 */
	@simpl_scalar
	private String							cSharpTypeName;
	
	/**
	 * Name for declaring the type in Objective C.
	 */
	@simpl_scalar
	private String							objectiveCTypeName;
	
	/**
	 * Empty constructor for S.IM.PL Serialization.
	 */
	public SimplType()
	{
		
	}
	/**
	 * Run-time constructor.
	 * 
	 * @param name 							The unique platform-independent identifier that S.IM.PL uses for the type name.
	 * @param simpleName TODO
	 * @param javaTypeName			Fully qualified name of the Java type that this represents, including package.
	 * @param cSharpTypeName 		Name for declaring the type in C#.
	 * @param objectiveCTypeName Name for declaring the type in Objective C.
	 */
	public SimplType(String name, String simpleName, String javaTypeName, String cSharpTypeName, String objectiveCTypeName)
	{
		super(name);
		
		this.simpleName					= simpleName;
		
		this.javaTypeName				= javaTypeName;
		this.cSharpTypeName			= cSharpTypeName;
		this.objectiveCTypeName	= objectiveCTypeName;
	}
	
	/**
	 * The full, qualified name of the class that this describes
	 * @return
	 */
	public String getJavaTypeName()
	{
		return javaTypeName;
	}
	/**
	 * The name to use when declaring a field in C# cross-compilation.
	 * 
	 * @return	cSharpTypeName, if one was passed in explicitly. otherwise, assume its the same as javaTypeName, and pass that.
	 */
	public String getCSharpTypeName()
	{
		return cSharpTypeName != null ? cSharpTypeName : javaTypeName;
	}
	/**
	 * The name to use when declaring a field in Objective C cross-compilation.
	 * 
	 * @return	objectiveCTypeName, , if one was passed in explicitly. otherwise, assume its the same as simple name, and pass that.
	 */
	public String getObjectiveCTypeName()
	{
		return objectiveCTypeName;
	}
	/**
	 * @return the simpleName
	 */
	public String getSimpleName()
	{
		return simpleName;
	}

	/**
	 * Empty base type implementation.
	 * 
	 * @return	always null.
	 */
	public String getDbTypeName()
	{
		return null;
	}

}
