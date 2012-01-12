package ecologylab.serialization.library.html;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

public class HtmlElement extends ElementState
{
	@simpl_scalar
	String	id;

	@simpl_tag("class")
	@simpl_scalar
	String	cssClass;

	@simpl_tag("itemprop")
	@simpl_scalar
	String	schemaOrgItemProp;
	
	@simpl_tag("itemscope itemtype")
	@simpl_scalar
	String schemaOrgItemType;

	public HtmlElement()
	{
	}

	public String getCssClass()
	{
		return cssClass;
	}

	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the schemaOrgItemProp
	 */
	public String getSchemaOrgItemProp()
	{
		return schemaOrgItemProp;
	}

	/**
	 * @param schemaOrgItemProp the schemaOrgItemProp to set
	 */
	public void setSchemaOrgItemProp(String schemaOrgItemProp)
	{
		this.schemaOrgItemProp = schemaOrgItemProp;
	}

	public String getSchemaOrgItemType()
	{
		return schemaOrgItemType;
	}

	public void setSchemaOrgItemType(String schemaOrgItemType)
	{
		this.schemaOrgItemType = schemaOrgItemType;
	}

}
