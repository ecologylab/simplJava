package simpl.descriptions.generics.testclasses;

import simpl.annotations.dbal.simpl_inherit;


@simpl_inherit
public class ImageSearch<I extends Image, X extends I, T extends MediaSearchResult<X>> extends MediaSearch<X, T>
{

}
