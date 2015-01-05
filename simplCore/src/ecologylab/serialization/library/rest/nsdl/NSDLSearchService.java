package ecologylab.serialization.library.rest.nsdl;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.library.rest.RestSearchResult;

/**
 * NSDL search REST wrapper.
 * @author blake
 *
 */
@simpl_inherit @simpl_tag("NSDLSearchService")
public class NSDLSearchService extends RestSearchResult
{
	public NSDLSearchService()
	{
		super();
	}
}
