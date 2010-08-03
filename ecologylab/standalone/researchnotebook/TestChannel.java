package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

public class TestChannel extends ElementState{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String title;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL link;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String description; 
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String language; 
	
	@simpl_nowrap
	@simpl_collection("test_item") ArrayList<TestItem> items = new ArrayList<TestItem>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ParsedURL getLink() {
		return link;
	}

	public void setLink(ParsedURL link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ArrayList<TestItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<TestItem> items) {
		this.items = items;
	} 
}
