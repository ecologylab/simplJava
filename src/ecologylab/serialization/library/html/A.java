package ecologylab.serialization.library.html;

import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class A extends HtmlElement
{
	@simpl_scalar
	private String	href;

	@simpl_scalar
	@simpl_hints(Hint.XML_TEXT)
	String link;
	
	public A()
	{
	}

	public String getHref()
	{
		return href;
	}

	public void setHref(String h)
	{
		href = h;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}
	public String toString()
	{
		String returnString = "";
		returnString += "a("+href+")["+link+"]";
		return returnString;
	}
}
