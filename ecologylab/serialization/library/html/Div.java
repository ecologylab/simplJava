package ecologylab.serialization.library.html;

public class Div extends HtmlElement
{
	private String CssClass;
	public Div()
	{
		CssClass = "";
		this.setId("");
	}

	public String getCssClass()
	{
		return CssClass;
	}

	public void setCssClass(String cssClass)
	{
		CssClass = cssClass;
	}
	
	public String open()
	{
		String open = "<div";
		if (this.getId() != null && this.getId().length() > 0)
			open += " id=\"" + this.getId() + "\"";
		if (this.getCssClass() != null && this.getCssClass().length() > 0)
			open += " class=\"" + this.getCssClass() + "\"";
		open += ">";
		return open;
	}
	
	public String close()
	{
		return "</div>";
	}
	
}
