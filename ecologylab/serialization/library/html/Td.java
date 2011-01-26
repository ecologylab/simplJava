package ecologylab.serialization.library.html;
import java.util.ArrayList;

public class Td extends HtmlElement
{
	String align;
	public String getAlign()
	{
		return align;
	}
	public void setAlign(String align)
	{
		this.align = align;
	}
	public Td(){}	
	public String open()
	{
		String open = "<td";
		if (this.getCssClass() != null && this.getCssClass().length() > 0)
			open += " class=\"" + this.getCssClass() + "\"";
		if (this.getAlign() != null && this.getAlign().length() > 0)
			open += "align=\"" + this.getAlign() +"\"";
		open += ">";
		return open;
	}
	
	public static String close()
	{
		return "</td>";
	}	
}
