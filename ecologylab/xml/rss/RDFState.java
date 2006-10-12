package ecologylab.xml.rss;

import java.util.Collection;

import ecologylab.xml.ArrayListState;
import ecologylab.xml.xml_inherit;

public @xml_inherit class RDFState extends ArrayListState
{
	
	public RDFState()
	{
		super();
	}
	protected Collection getCollection(Class thatClass)
	{
  		return Item.class.equals(thatClass) ?
		   super.getCollection(thatClass) : null;
  	}

}
