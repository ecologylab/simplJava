package ecologylab.serialization.library.html;

import java.util.ArrayList;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_classes;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;


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
	
	@Override
	public String toString()
	{
		String returnString = "";
		returnString += " div:"+text;
		for(Object member : members)
		{
			returnString += " "+member.toString()+",";
		}
		returnString += ":/div ";
		return returnString;
	}
}
