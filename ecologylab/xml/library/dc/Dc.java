package ecologylab.xml.dc;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.ElementState.xml_leaf;

/**
 * Dublin Core Metadata Element Set, as defined at
 * 
 * {@link http://dublincore.org/documents/dces/ http://dublincore.org/documents/dces/}.
 * 
 * There is no enclosing parent element in the XML markup.
 *
 * @author andruid
 */
public class Dc extends ElementState
{
	@xml_leaf	public String				title;
	@xml_leaf	public String				creator;
	@xml_leaf	public String				subject;
	@xml_leaf	public String				description;
	@xml_leaf	public String				publisher;
	// Contributor
	// public Date 			date; // we need to implement a Date type!
	// values for type: Collection, Dataset, Event, Image, MovingImage, 
	// InteractiveResource, PhysicalObject, Service, Software, Sound,
	// StillImage, Text
	@xml_leaf	public String				type;
	/**
	 * Probably the mime-type, but alas not necessarily.
	 */
	@xml_leaf	public String				format;
	/**
	 * Recommended best practice is to identify the resource by means of a string or number conforming to a formal identification system. Formal identification systems include but are not limited to the Uniform Resource Identifier (URI) (including the Uniform Resource Locator (URL)), the Digital Object Identifier (DOI) and the International Standard Book Number (ISBN).
	 */
	@xml_leaf	public ParsedURL			identifier;
	// language
	// relation
	// coverage
	// rights
	
	// from http://memory.loc.gov/ammem/award/docs/dublin-examples.html#thirteen
	
	// public String		author;
	// public String otherAgent
	// public String		otherType;
	// public String		form; // instead of format
	   
	static final String[]		LEAF_ELEMENT_FIELD_NAMES	= 
	{"title", "creator", "description", "subject", "publisher", "type",
	 "format", "identifier"};
	
	/**
	 * The array of Strings with the names of the leaf elements.
	 * 
	 * @return
	 */
	protected String[] leafElementFieldNames()
	{
		return LEAF_ELEMENT_FIELD_NAMES;
	}

	
	private static final String TRANSLATION_SPACE_NAME	= "dc";
	private static final String PACKAGE_NAME			= "ecologylab.xml.dc";

	public static final Class TRANSLATIONS[]	= 
	{
		Dc.class,
	};

	public static TranslationSpace get()
	{
		return TranslationSpace.get(TRANSLATION_SPACE_NAME, PACKAGE_NAME, TRANSLATIONS);
	}
	
}
