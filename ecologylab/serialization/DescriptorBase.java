/**
 * 
 */
package ecologylab.serialization;

import ecologylab.serialization.ElementState.simpl_scalar;

/**
 * Common code for ClassDescriptor and FieldDescriptor.
 * 
 * @author andruid
 */
public class DescriptorBase extends ElementState
{
	/**
	 * The tag name that this field is translated to XML with. For polymorphic fields, the value of
	 * this field is meaningless, except for wrapped collections and maps.
	 */
	@simpl_scalar
	protected String							tagName;

	@simpl_scalar
	protected String 							comment;
	
	protected static IJavaParser	javaParser;
	

	/**
	 * 
	 */
	public DescriptorBase()
	{
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * NB: For polymorphic fields, the value of this field is meaningless, except for wrapped
	 * collections and maps.
	 * 
	 * @return The tag name that this field is translated to XML with.
	 */
	public String getTagName()
	{
		return tagName;
	}

	public static void setJavaParser(IJavaParser javaParser)
	{
		DescriptorBase.javaParser = javaParser;
	}
	
	/**
	 * 
	 * @return the java doc comment
	 */
	public String getComment()
	{
		return comment;
	}

}
