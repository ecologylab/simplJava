package ecologylab.serialization.library.dc;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;
import simpl.core.ElementState;
import simpl.core.SimplTypesScope;
import ecologylab.net.ParsedURL;

/**
 * Dublin Core Metadata Element Set, as defined at
 * 
 * 
 * 
 * There is no enclosing parent element in the XML markup.
 *
 * @author andruid
 */
public class DcTag extends ElementState
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) @simpl_tag("dc:title") 		String				title;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) @simpl_tag("dc:creator") 	String				creator;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) @simpl_tag("dc:subject")	String				subject;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) @simpl_tag("dc:description")String				description;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) @simpl_tag("dc:publisher")	String				publisher;
	// Contributor
	// public Date 			date; // we need to implement a better Date type!
	// values for type: Collection, Dataset, Event, Image, MovingImage, 
	// InteractiveResource, PhysicalObject, Service, Software, Sound,
	// StillImage, Text
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) @simpl_tag("dc:type")		String				type;
	/**
	 * Probably the mime-type, but alas not necessarily.
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) @simpl_tag("dc:format")		String				format;
	/**
	 * Recommended best practice is to identify the resource by means of a string or number conforming to a formal identification system. Formal identification systems include but are not limited to the Uniform Resource Identifier (URI) (including the Uniform Resource Locator (URL)), the Digital Object Identifier (DOI) and the International Standard Book Number (ISBN).
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) @simpl_tag("dc:identifier")	ParsedURL			identifier;
	// language
	// relation
	// coverage
	// rights
	
	// from http://memory.loc.gov/ammem/award/docs/dublin-examples.html#thirteen
	
	// public String		author;
	// public String otherAgent
	// public String		otherType;
	// public String		form; // instead of format
	   
	private static final String TRANSLATION_SPACE_NAME	= "dc";
	private static final String PACKAGE_NAME			= "ecologylab.serialization.library.dc";

	public static final Class<?> TRANSLATIONS[]	= 
	{
		Dc.class,
	};

	public static SimplTypesScope get()
	{
		return SimplTypesScope.get(TRANSLATION_SPACE_NAME, TRANSLATIONS);
	}
	
	public DcTag() 
	{
		
	}

	/**
	 * @return Returns the creator.
	 */
	public String getCreator()
	{
		return creator;
	}

	/**
	 * @param creator The creator to set.
	 */
	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return Returns the format.
	 */
	public String getFormat()
	{
		return format;
	}

	/**
	 * @param format The format to set.
	 */
	public void setFormat(String format)
	{
		this.format = format;
	}

	/**
	 * @return Returns the identifier.
	 */
	public ParsedURL getIdentifier()
	{
		return identifier;
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(ParsedURL identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * @return Returns the publisher.
	 */
	public String getPublisher()
	{
		return publisher;
	}

	/**
	 * @param publisher The publisher to set.
	 */
	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}

	/**
	 * @return Returns the subject.
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	
	@Override
	public String toString()
	{
		return "Dc {\n" + 
			"creator: " 	+ creator		+ "\n" +
			"description: " + description	+ "\n" +
			"format: " 		+ format		+ "\n" +
			"identifier: " 	+ identifier	+ "\n" +
			"publisher: " 	+ publisher		+ "\n" +
			"subject: " 	+ subject		+ "\n" +
			"title: " 		+ title			+ "\n" +
			"type: " 		+ type			+ "\n" +
		"}";
			
	}
}
