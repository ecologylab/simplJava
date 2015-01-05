package legacy.tests.generics;

import ecologylab.serialization.annotations.simpl_inherit;

@simpl_inherit
public class ImageSearch<I extends Image, X extends I, T extends MediaSearchResult<X>> extends MediaSearch<X, T>
{

}
