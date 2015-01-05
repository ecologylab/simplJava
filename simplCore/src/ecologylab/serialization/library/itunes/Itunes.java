package ecologylab.serialization.library.itunes;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * XMLNS (namespace) corresponding to itunes podcasts.
 * 
 * http://www.apple.com/itunes/podcasts/techspecs.html
 * 
 * It seems that between them, apple and yahoo should have one standard.
 * They suck for having two similar but different ones.
 *
 * @author andruid
 */
public class Itunes extends ElementState
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			subtitle;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			author;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			summary;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	ParsedURL		image;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			duration;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			keywords;
	//public Owner			owner; subfields -- email, name
	/**
	 * @return Returns the author.
	 */
	protected String getAuthor()
	{
		return author;
	}
	/**
	 * @return Returns the duration.
	 */
	protected String getDuration()
	{
		return duration;
	}
	/**
	 * @return Returns the image.
	 */
	protected ParsedURL getImage()
	{
		return image;
	}
	/**
	 * @return Returns the keywords.
	 */
	protected String getKeywords()
	{
		return keywords;
	}
	/**
	 * @return Returns the subtitle.
	 */
	protected String getSubtitle()
	{
		return subtitle;
	}
	/**
	 * @return Returns the summary.
	 */
	protected String getSummary()
	{
		return summary;
	}
	
	/**
	 * Cateogory is a yuck field to define semantically, because there
	 * can be a single one at top level.
	 * There can be multiple ones at top level.
	 * And, additionally, they can be nested. It seems that in practice,
	 * their categories hierarchy only goes one deep at this time.
	 * <p/>
	 * Yuck!
	 * <p/>
	 * It seems that these are quite similar to the Category sub-element of
	 * media:content, except that
	 * 1) these can be nested
	 * 2) these use an attribute field named "text", instead of functioning as
	 * plain old leaf nodes. Triple yuck.
	 */
	//public Category			cateogry;
	
	// public String			explicit;
	
}
