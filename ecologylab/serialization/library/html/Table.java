package ecologylab.serialization.library.html;

import java.util.ArrayList;

import ecologylab.serialization.simpl_inherit;

@simpl_inherit
public class Table extends HtmlElement
{
	@simpl_nowrap
	@simpl_collection("tr")	
	public ArrayList<Tr>	rows;

	public Table()
	{
		rows = new ArrayList<Tr>();
	}
}
