package ecologylab.serialization.library.rest.nsdl;

import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.library.rest.RestSearchResult;

/**
 * NSDL search REST wrapper.
 * @author blake
 *
 */
@simpl_inherit @xml_tag("NSDLSearchService")
public class NSDLSearchService extends RestSearchResult
{
	public NSDLSearchService()
	{
		super();
	}
}
