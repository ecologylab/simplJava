package ecologylab.standalone.researchnotebook;

import java.util.Date;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

// TestChannel.java 
public class TestItem extends ElementState{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String title; 
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL link;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String description; 
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String guid; 
	@xml_tag("pubDate") @simpl_scalar @simpl_hints(Hint.XML_LEAF) String pubDate;
	
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
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	} 
}
