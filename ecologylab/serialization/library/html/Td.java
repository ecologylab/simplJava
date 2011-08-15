package ecologylab.serialization.library.html;

import java.util.ArrayList;

import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class Td extends HtmlElement
{
	@simpl_scalar
	String										align;

	@simpl_classes(
	{ HtmlElement.class, Table.class, Tr.class, Span.class, Div.class, Td.class, A.class })
	@simpl_collection
	@simpl_nowrap
	public ArrayList<Object>	items	= new ArrayList<Object>();
	
	@simpl_nowrap
	@simpl_collection("span")
	public ArrayList<String> spans = new ArrayList<String>();
	
	public String getAlign()
	{
		return align;
	}

	public void setAlign(String align)
	{
		this.align = align;
	}

	public Td()
	{
	}
}
