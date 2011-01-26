package ecologylab.serialization.library.html;


public class Tr extends HtmlElement
{
	public Tr()
	{
		this.setId("");
		this.setCssClass("");
	}
	public String open()
	{
		String open = "<tr";
		if (this.getId() != null && this.getId().length() > 0)
			open += " id=\"" + this.getId() + "\"";
		if (this.getCssClass() != null && this.getCssClass().length() > 0)
			open += " class=\"" + this.getCssClass() + "\"";
		open += ">";
		
		return open;
	}
	
	public static String close()
	{
		return "</tr>";

	}
}
