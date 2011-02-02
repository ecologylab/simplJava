package ecologylab.tests;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementState.bibtex_type;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

@simpl_inherit
@xml_tag("bibtex_entity")
@bibtex_type("inproceedings")
public class BibTeXEntity extends ElementState
{

	@simpl_scalar
	@bibtex_key
	private String citationKey;
	
	@simpl_scalar
	@bibtex_tag("author")
	private String author;
	
	@simpl_scalar
	@bibtex_tag("title")
	private String title;
	
	@simpl_scalar
	@bibtex_tag("booktitle")
	private String bookTitle;
	
	@simpl_scalar
	@bibtex_tag("publisher")
	private String publisher;
	
	@simpl_scalar
	@bibtex_tag("year")
	private int year;
	
	@simpl_scalar
	@bibtex_tag("doi")
	private String doi;

	public String getCitationKey()
	{
		return citationKey;
	}

	public void setCitationKey(String citationKey)
	{
		this.citationKey = citationKey;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getPublisher()
	{
		return publisher;
	}

	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public String getBookTitle()
	{
		return bookTitle;
	}

	public void setBookTitle(String bookTitle)
	{
		this.bookTitle = bookTitle;
	}

	public String getDoi()
	{
		return doi;
	}

	public void setDoi(String doi)
	{
		this.doi = doi;
	}
	
}
