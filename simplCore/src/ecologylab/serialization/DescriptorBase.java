/**
 * 
 */
package ecologylab.serialization;

import java.util.ArrayList;
import java.util.List;

import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;
import simpl.types.SimplBaseType;


/**
 * Common code for ClassDescriptor and FieldDescriptor.
 * 
 * @author andruid
 */
@simpl_inherit
public abstract class DescriptorBase extends SimplBaseType
{
	
	/**
	 * The tag name that this field is translated to XML with. For polymorphic fields, the value of
	 * this field is meaningless, except for wrapped collections and maps.
	 */
	@simpl_scalar
	protected String								tagName;

	/**
	 * Used to specify old translations, for backwards compatability. Never written.
	 */
	@simpl_nowrap
	@simpl_collection("other_tag")
	protected ArrayList<String>			otherTags;

	/**
	 * Documentation comment for a class or field.
	 */
	@simpl_scalar
	protected String								comment;

	protected List<MetaInformation>	metaInfo;

	protected static IJavaParser		javaParser;

	/**
	 * 
	 */
	public DescriptorBase()
	{
		super();
	}
	
	public DescriptorBase(String tagName, String name)
	{
		this(tagName, name, null);
	}
	
	public DescriptorBase(String tagName, String name, String comment)
	{
		super(name);
		this.comment		= comment;
		this.tagName		= tagName;
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
	
	protected void setTagName(String tagName)
	{
		this.tagName = tagName;
	}
	
	abstract public ArrayList<String> otherTags();
	
	/**
	 * 
	 * @return the java doc comment
	 */
	public String getComment()
	{
		return comment;
	}

	public static void setJavaParser(IJavaParser javaParser)
	{
		DescriptorBase.javaParser = javaParser;
	}

}
