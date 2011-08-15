package ecologylab.serialization.library.html;

import java.util.ArrayList;

import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;

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
		cssClass = "";
		this.setId("");
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
