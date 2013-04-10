package ecologylab.serialization.library.rest.nsdl;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_tag;
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
