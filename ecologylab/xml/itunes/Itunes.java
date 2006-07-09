package ecologylab.xml.itunes;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementStateWithLeafElements;

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
public class Itunes extends ElementStateWithLeafElements
{
	public String			subtitle;
	public String			author;
	public String			summary;
	public ParsedURL		image;
	public String			duration;
	public String			keywords;
	//public Owner			owner; subfields -- email, name
	
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
	
	static final String[]		LEAF_ELEMENT_FIELD_NAMES	= 
	{"subtitle", "author", "summary", "image", "duration", "keywords"};
	
	static
	{
		defineLeafElementFieldNames(LEAF_ELEMENT_FIELD_NAMES);
	}
	
	
}
