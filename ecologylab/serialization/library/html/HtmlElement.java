package ecologylab.serialization.library.html;

import ecologylab.serialization.ElementState;

public class HtmlElement extends ElementState
{
	String id;
	String CssClass;
	public String getCssClass()
	{
		return CssClass;
	}

	public void setCssClass(String cssClass)
	{
		CssClass = cssClass;
	}

	HtmlElement() {}
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}

}
