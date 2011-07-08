package ecologylab.serialization.library.html;

import ecologylab.serialization.ElementState;

public class HtmlElement extends ElementState
{
	@simpl_scalar
	String	id;

	@xml_tag("class")
	@simpl_scalar
	String	cssClass;

	@xml_tag("itemprop")
	@simpl_scalar
	String	schemaOrgItemProp;
	
	@xml_tag("itemscope itemtype")
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
