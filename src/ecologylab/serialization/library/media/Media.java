package ecologylab.serialization.library.media;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;

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
	@simpl_composite Content		content;
	
	@simpl_composite Credit		credit;
	
	@simpl_composite	Thumbnail	thumbnail;
	
	@simpl_composite	Group		group;
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String		title;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String		description;
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String		category;
	
	//FIXME -- need to implement @simpl_scalar @simpl_hints(Hint.XML_TEXT) directive to parse this properly.
	// not a leaf! may have type attribute.
	// it would also be very nice if the parser knew to warn about and then
	// throw away attributes found inside elements declared as leaf.
	
//	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String		text;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String		rating;
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
	public static final SimplTypesScope getTranslations()
	{
		return SimplTypesScope.get(TRANSLATION_SPACE_NAME, TRANSLATIONS);
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
