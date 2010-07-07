package ecologylab.serialization.library.rest;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

public class Header extends ElementState
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) 	protected	String	identifier;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) 	protected	String	lastIndexed;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) 	protected	String	metadataLastModified;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) 	protected	String	contentLastModified;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) 	protected	String	contentLastFetched;
	
	public Header() {}
	
	public Header(String identifier, String lastIndexed, String metadataLastModified,
					String contentLastModified, String contentLastFetched)
	{
		this.identifier				= identifier;
		this.lastIndexed			= lastIndexed;
		this.metadataLastModified	= metadataLastModified;
		this.contentLastModified	= contentLastModified;
		this.contentLastFetched		= contentLastFetched;
	}
}
