package ecologylab.xml.rss;

import java.util.Collection;

import ecologylab.xml.ArrayListState;

public class RDFState extends ArrayListState
{
	
	public RDFState()
	{
		super();
	}
	protected Collection getCollection(Class thatClass, String tag)
	{
  		return Item.class.equals(thatClass) ?
		   super.getCollection(thatClass, tag) : null;
  	}

}
