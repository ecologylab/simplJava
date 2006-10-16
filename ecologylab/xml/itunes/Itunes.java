package ecologylab.xml.itunes;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_leaf;

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
	@xml_leaf	public String			subtitle;
	@xml_leaf	public String			author;
	@xml_leaf	public String			summary;
	@xml_leaf	public ParsedURL		image;
	@xml_leaf	public String			duration;
	@xml_leaf	public String			keywords;
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
	
}
