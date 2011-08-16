package ecologylab.serialization.bibtex.entrytypes;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.bibtex_key;
import ecologylab.serialization.annotations.bibtex_tag;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * The abstract base class for BibTeX entry types. Holds the citation key and common fields such
 * as title, authors, year, etc.
 * 
 * @author quyin
 *
 */
@simpl_inherit
public abstract class AbstractBibTeXEntry extends ElementState
{

	// citation key

	@simpl_scalar
	@bibtex_key
	private String						citationKey;

	// required fields

	@simpl_scalar
	@bibtex_tag("title")
	private String						title;

	@simpl_collection("author")
	@bibtex_tag("author")
	private ArrayList<String>	authors;

	@simpl_scalar
	@bibtex_tag("year")
	private int								year;

	// optional fields

	@simpl_scalar
	@bibtex_tag("month")
	private String						month;

	@simpl_scalar
	@bibtex_tag("note")
	private String						note;

	@simpl_scalar
	@bibtex_tag("key")
	private String						key;

	@simpl_collection("keyword")
	@bibtex_tag("keywords")
	private ArrayList<String>	keywords;

	@simpl_scalar
	@bibtex_tag("doi")
	private String						doi;

	public String getCitationKey()
	{
		return citationKey;
	}

	public void setCitationKey(String citationKey)
	{
		this.citationKey = citationKey;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public ArrayList<String> getAuthors()
	{
		return authors;
	}

	public void setAuthors(ArrayList<String> authors)
	{
		this.authors = authors;
	}

	public String getMonth()
	{
		return month;
	}

	public void setMonth(String month)
	{
		this.month = month;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public ArrayList<String> getKeywords()
	{
		return keywords;
	}

	public void setKeywords(ArrayList<String> keywords)
	{
		this.keywords = keywords;
	}

	public String getDoi()
	{
		return doi;
	}

	public void setDoi(String doi)
	{
		this.doi = doi;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public int getYear()
	{
		return year;
	}

}
