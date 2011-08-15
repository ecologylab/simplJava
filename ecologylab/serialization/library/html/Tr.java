package ecologylab.serialization.library.html;

import java.util.ArrayList;

import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_nowrap;

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
}
