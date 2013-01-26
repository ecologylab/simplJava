package ecologylab.serialization.library.html;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;

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
	@Override
	public String toString()
	{
		String returnString = "";
		returnString += "a("+href+")["+link+"]";
		return returnString;
	}
}
