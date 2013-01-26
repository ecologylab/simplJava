package legacy.tests.generics;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_inherit;

@simpl_inherit
public class MediaSearch<M extends Media, T extends MediaSearchResult<? extends M>> extends Search<T>
{

	@simpl_composite
	MediaSearchResult<Media>	firstResult;

}
