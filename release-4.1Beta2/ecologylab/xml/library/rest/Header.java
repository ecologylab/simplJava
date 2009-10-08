package ecologylab.xml.library.rest;

import ecologylab.xml.ElementState;

public class Header extends ElementState
{
	@xml_leaf 	protected	String	identifier;
	@xml_leaf 	protected	String	lastIndexed;
	@xml_leaf 	protected	String	metadataLastModified;
	@xml_leaf 	protected	String	contentLastModified;
	@xml_leaf 	protected	String	contentLastFetched;
	
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
