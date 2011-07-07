/**
 * 
 */
package ecologylab.serialization;

import java.util.ArrayList;

/**
 * Common code for ClassDescriptor and FieldDescriptor.
 * 
 * @author andruid
 */
public abstract class DescriptorBase extends ElementState
{
	/**
	 * The tag name that this field is translated to XML with. For polymorphic fields, the value of
	 * this field is meaningless, except for wrapped collections and maps.
	 */
	@simpl_scalar
	protected String							tagName;

	/**
	 * Used to specify old translations, for backwards compatability. Never written.
	 */
	@simpl_nowrap
	@simpl_collection("other_tag")
	protected ArrayList<String>		otherTags;

	@simpl_scalar
	protected String							comment;

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
	
	abstract public ArrayList<String> otherTags();
	
	protected void addOtherTag(String otherTag)
	{
		if (this.otherTags == null)
			this.otherTags = new ArrayList<String>();
		if (!this.otherTags.contains(otherTag))
			this.otherTags.add(otherTag);
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
