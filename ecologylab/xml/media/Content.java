package ecologylab.xml.media;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_leaf;

/**
 * Primary element of the media XML name space. As in <media:content>
 *
 * @author andruid
 */
public class Content extends ElementState
{
	public ParsedURL		url;
	public String			type;
	public int				width;
	public int				height;
	
	@xml_leaf	public String			title;
	public Description		description;
	public String			keywords;
	public Thumbnail		thumbnail;
	
	// there can be 0 or more elements of tag "category"
	// we will add these to a collection automatically by overriding setField(Field, String)
	//TODO confirm if this is correct.
	public ArrayList		categoryStrings;
	
	public Credit			credit;
	
	//public String 		text -- actually there can be many of these
	//public String			restriction; // a leaf node
	//
	

	static final String[]		LEAF_ELEMENT_FIELD_NAMES	= {"title", "category"};
	
	/**
	 * The array of Strings with the names of the leaf elements.
	 * 
	 * @return
	 */
	protected String[] leafElementFieldNames()
	{
		return LEAF_ELEMENT_FIELD_NAMES;
	}
	
	public Content()
	{
		super();
	}

	/**
	 * Add cateogry leaf nodes as attribute values into categoryStrings.
	 * Otherwise, call super() to do regular field setting,
	 * using reflection
	 */
	protected boolean setFieldUsingTypeRegistry(Field field, String fieldValue)
	{
		String fieldName	= field.getName();
		if ("cateogry".equalsIgnoreCase(fieldName))
		{
			this.evalCategoryStrings().add(fieldValue);
			return true;
		}
		else
			return super.setFieldUsingTypeRegistry(field, fieldValue);
	}

	/**
	 * Lazy evaluation avoids unnecessary allocations.
	 * 
	 * Note: a different accessor is needed for external calls --
	 * one that won't allocate on demand.
	 * 
	 * @return Returns the categoryStrings.
	 */
	protected ArrayList evalCategoryStrings()
	{
		ArrayList result	= categoryStrings;
		if (categoryStrings == null)
		{
			result			= new ArrayList(5);
			categoryStrings	= result;
		}
		return result;
	}

	
}
