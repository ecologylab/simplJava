package legacy.tests.generics;

import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_inherit;

@simpl_inherit
public class MediaSearch<M extends Media, T extends MediaSearchResult<? extends M>> extends Search<T>
{

	@simpl_composite
	MediaSearchResult<Media>	firstResult;

}
