package ecologylab.serialization.library.html;

import ecologylab.serialization.ElementState;

public class HtmlElement extends ElementState
{
	@simpl_scalar
	String	id;

	@xml_tag("class")
	@simpl_scalar
	String	CssClass;

	public HtmlElement()
	{
	}

	public String getCssClass()
	{
		return CssClass;
	}

	public void setCssClass(String cssClass)
	{
		CssClass = cssClass;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

}
