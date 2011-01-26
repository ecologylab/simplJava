package ecologylab.serialization.library.html;

public class Span extends HtmlElement
{
	public Span(){}
	
	public String open()
	{
		String open = "<span";
		if (this.getCssClass() != null && this.getCssClass().length() > 0)
			open += " class=\"" + this.getCssClass() + "\"";
		open += ">";
		return open;
	}
	
	public String close()
	{
		return "</span>";
	}

}
