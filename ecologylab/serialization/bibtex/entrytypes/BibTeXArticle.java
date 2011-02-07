package ecologylab.serialization.bibtex.entrytypes;

import ecologylab.serialization.ElementState.bibtex_type;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

@simpl_inherit
@xml_tag("bibtex_article")
@bibtex_type("article")
public class BibTeXArticle extends AbstractBibTeXEntry
{

	// required fields

	@simpl_scalar
	@bibtex_tag("journal")
	private String	journal;

	@simpl_scalar
	@bibtex_tag("year")
	private int			year;

	// optional fields

	@simpl_scalar
	@bibtex_tag("volume")
	private String	volume;

	@simpl_scalar
	@bibtex_tag("number")
	private int			number;

	@simpl_scalar
	@bibtex_tag("pages")
	private String	pages;

	public String getJournal()
	{
		return journal;
	}

	public void setJournal(String journal)
	{
		this.journal = journal;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public String getVolume()
	{
		return volume;
	}

	public void setVolume(String volume)
	{
		this.volume = volume;
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public String getPages()
	{
		return pages;
	}

	public void setPages(String pages)
	{
		this.pages = pages;
	}

}
