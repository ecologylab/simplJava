package ecologylab.xml.library.media;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.ElementState.xml_leaf;
import ecologylab.xml.library.dc.Dc;
import ecologylab.xml.library.rss.Channel;
import ecologylab.xml.library.rss.Item;
import ecologylab.xml.library.rss.RDFState;
import ecologylab.xml.library.rss.RssState;

/**
 * Root class for inserting elements from the Yahoo Media XML Namespace.
 * 
 * See	http://search.yahoo.com/mrss
 *
 * @author andruid
 */
public class Media extends ElementState
{
	/**
	 * A single Content element in the Yahoo schema.
	 * <p/>
	 * An alternative would be to implement this as an ArrayListState, and be able
	 * to aggregate a set of these. We can do this when there is a need, with an example.
	 */
	@xml_nested Content		content;
	
	@xml_nested Credit		credit;
	
	@xml_nested	Thumbnail	thumbnail;
	
	@xml_nested	Group		group;
	
	@xml_leaf	String		title;
	@xml_leaf	String		description;
	
	@xml_leaf	String		category;
	
	//FIXME -- need to implement @xml_text directive to parse this properly.
	// not a leaf! may have type attribute.
	// it would also be very nice if the parser knew to warn about and then
	// throw away attributes found inside elements declared as leaf.
	
//	@xml_leaf	String		text;
	@xml_leaf	String		rating;
	/**
	 * @return Returns the category.
	 */
	public String getCategory()
	{
		return category;
	}
	/**
	 * @param category The category to set.
	 */
	public void setCategory(String category)
	{
		this.category = category;
	}
	/**
	 * @return Returns the content.
	 */
	public Content getContent()
	{
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(Content content)
	{
		this.content = content;
	}
	/**
	 * @return Returns the rating.
	 */
	public String getRating()
	{
		return rating;
	}
	/**
	 * @param rating The rating to set.
	 */
	public void setRating(String rating)
	{
		this.rating = rating;
	}
	/**
	 * @return Returns the text.
	 */

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the credit.
	 */
	public Credit getCredit()
	{
		return credit;
	}
	/**
	 * @param credit The credit to set.
	 */
	public void setCredit(Credit credit)
	{
		this.credit = credit;
	}
	
	private static final String TRANSLATION_SPACE_NAME	= "yahoo_media";

	public static final Class TRANSLATIONS[]	= 
	{
		Media.class,
		Thumbnail.class,
		Content.class,
		Description.class,
		Credit.class,
		
	};

	/**
	 * TranslationSpace for Yahoo Media.
	 * 
	 * @return
	 */
	public static final TranslationSpace getTranslations()
	{
		return TranslationSpace.get(TRANSLATION_SPACE_NAME, TRANSLATIONS);
	}
	/**
	 * @return the thumbnail
	 */
	public Thumbnail getThumbnail()
	{
		Thumbnail result	= thumbnail;
		
		if (result == null)
		{
			Group group				= this.group;
			if (group != null)
			{
				Media nestedMedia	= group.lookupMedia();
				if (nestedMedia != null)
					result			= nestedMedia.getThumbnail();
			}
		}
		
		return result;
	}
	/**
	 * @param thumbnail the thumbnail to set
	 */
	public void setThumbnail(Thumbnail thumbnail)
	{
		this.thumbnail = thumbnail;
	}
	/**
	 * @return the group
	 */
	public Group getGroup()
	{
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(Group group)
	{
		this.group = group;
	}
}
