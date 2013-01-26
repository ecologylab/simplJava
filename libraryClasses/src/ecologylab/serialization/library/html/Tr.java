package ecologylab.serialization.library.html;

import java.util.ArrayList;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_nowrap;


@simpl_inherit
public class Tr extends HtmlElement
{
	@simpl_nowrap
	@simpl_collection("td")
	@simpl_hints(Hint.XML_LEAF)
	public ArrayList<Td>	cells;

	public Tr()
	{
		this.setId("");
		this.setCssClass("");
		cells = new ArrayList<Td>();
	}
	
	boolean printOnceLock = false;
	@Override
	public String toString()
	{
		if(printOnceLock)
		   return "tr-lockreached";	
		String returnString = "tr[";
		for(Td td : cells)
		{
			returnString += " "+td.toString();
		}
		returnString += " ]"+"\n";
		printOnceLock = false;
		return returnString;
	}
}
