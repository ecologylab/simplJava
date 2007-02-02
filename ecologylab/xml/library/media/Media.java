package ecologylab.xml.library.media;

import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_leaf;

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
	
	//public Credit		credit;
	@xml_leaf	String		title;
	@xml_leaf	String		description;
	
	@xml_leaf	String		category;
	
	@xml_leaf	String		text;
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
	public String getText()
	{
		return text;
	}
	/**
	 * @param text The text to set.
	 */
	public void setText(String text)
	{
		this.text = text;
	}
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

}
