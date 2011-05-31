package ecologylab.serialization.library.html;

import java.util.ArrayList;

import ecologylab.serialization.Hint;
import ecologylab.serialization.simpl_inherit;

@simpl_inherit
public class Div extends HtmlElement
{
	@simpl_classes(
	{ HtmlElement.class, Table.class, Tr.class, Span.class, Div.class, Td.class, A.class })
	@simpl_collection
	@simpl_nowrap
	public ArrayList<Object>	members	= new ArrayList<Object>();
	
	@simpl_scalar
	@simpl_hints(Hint.XML_TEXT)
	String text;

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

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
}
