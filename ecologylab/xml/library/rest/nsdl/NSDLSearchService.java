package ecologylab.xml.library.rest.nsdl;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;
import ecologylab.xml.library.rest.RestSearchResult;

/**
 * NSDL search REST wrapper.
 * @author blake
 *
 */
@xml_inherit @xml_tag("NSDLSearchService")
public class NSDLSearchService extends RestSearchResult
{
	public NSDLSearchService()
	{
		super();
	}
}
