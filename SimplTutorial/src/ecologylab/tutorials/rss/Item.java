 //Item.java

package ecologylab.tutorials.rss;

import java.util.ArrayList;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_collection;

public class Item extends ElementState
{
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

	public ParsedURL getLink() {
		return link;
	}

	public void setLink(ParsedURL link) {
		this.link = link;
	}

	public ParsedURL getGuid() {
		return guid;
	}

	public void setGuid(ParsedURL guid) {
		this.guid = guid;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public ArrayList<String> getCategorySet() {
		return categorySet;
	}

	public void setCategorySet(ArrayList<String> categorySet) {
		this.categorySet = categorySet;
	}

@simpl_scalar @simpl_hints(Hint.XML_LEAF) String      title;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF) String      description;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL    link;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL    guid;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF) String      author;
   
   @simpl_nowrap
   @simpl_collection("category") 
   ArrayList<String>    categorySet;
   
   public Item() {}
}