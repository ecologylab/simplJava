package ecologylab.serialization.library.html;

import java.util.ArrayList;

public class Table extends HtmlElement
{
	public Table(){}
	public String open()
	{
		return "<table>";
	}
	
	public String close()
	{
		return "</table>";
	}
}
