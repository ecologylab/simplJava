/**
 * 
 */
package ecologylab.tests.serialization;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * @author andruid
 *
 */
@simpl_tag("item")
public class ItemTest extends Base
{
  @simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			title;
  @simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			description;
  /**
   * This version of link often has a special url with rss in it.
   */
  @simpl_scalar @simpl_hints(Hint.XML_LEAF)	ParsedURL		link;
  /**
   * This seems to be the version of link that users want to see.
   */
  @simpl_scalar @simpl_hints(Hint.XML_LEAF)	ParsedURL		guid;

	/**
	 * 
	 */
	public ItemTest()
	{
		// TODO Auto-generated constructor stub
	}

}
