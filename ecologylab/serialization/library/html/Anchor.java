package ecologylab.serialization.library.html;

public class Anchor extends HtmlElement
{
	private String href;
	public Anchor(){}
	
	public String getHref()
	{
		return href;
	}
	
	public void setHref(String h)
	{
		href = h;
	}
	
	public String open()
	{
		String open = "<a target=\"_blank\" href=\"";
		return open;
	}
	
	public static String close()
	{
		return "</a>";
	}
}
