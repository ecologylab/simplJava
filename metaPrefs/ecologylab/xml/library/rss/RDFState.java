package ecologylab.xml.library.rss;

import java.util.Collection;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * Alternative root element
 * {@link ecologylab.xml.ElementState ElementState} declarations for RSS parser:
 * that nasty RSS versions: .90 and 1.0.
 * <p/>
 * This is a bit of a hack, in that it makes no attempt to handle general RDF, or to
 * support namespace definitions with great depth.
 * <p/>
 * Those things can be done with this framework. One of these days, an application will
 * drive someone to develop such extensions.
 *
 * @author andruid
 */

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
