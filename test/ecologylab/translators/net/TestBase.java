package ecologylab.translators.net;

import java.util.List;

import ecologylab.semantics.metadata.Metadata;
import ecologylab.semantics.metadata.scalar.MetadataInteger;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * Base class for testing C# code generator.
 * 
 * @author quyin
 *
 */
@simpl_inherit
public class TestBase extends ElementState
{
	
	@simpl_scalar
	private int scalar1;
	
	@simpl_scalar
	private String scalar2;
	
	@simpl_collection("number")
	private List<MetadataInteger> collection1;
	
	@simpl_tag("MyCollection")
	@simpl_collection("relatedMetadata")
	private List<Metadata> collection2;
	
}
