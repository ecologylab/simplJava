package ecologylab.serialization.library.html;

import java.util.ArrayList;

import simpl.annotations.dbal.simpl_classes;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;


@simpl_inherit
public class Td extends HtmlElement
{
	@simpl_scalar
	String										align;

	@simpl_classes(
	{ HtmlElement.class, Table.class, Tr.class, Div.class, Td.class, A.class })
	@simpl_collection
	@simpl_nowrap
	public ArrayList<Object>	items	= new ArrayList<Object>();
	
	/*
	@simpl_nowrap
	@simpl_collection("span")
	public ArrayList<String> spans = new ArrayList<String>();
	*/
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
	
	boolean printOnceLock = false;
	@Override
	public String toString()
	{
		if(printOnceLock)
		   return "td-lockreached";	
		String returnString = "td|";
		for(Object item : items)
		{
			returnString += " "+item.toString();
		}
		returnString += " |";
		printOnceLock = false;
		return returnString;
	}
}
