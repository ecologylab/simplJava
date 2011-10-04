package ecologylab.serialization.deserializers.parsers.bibtex.entrytypes;

import ecologylab.serialization.annotations.bibtex_tag;
import ecologylab.serialization.annotations.bibtex_type;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

@simpl_inherit
@simpl_tag("bibtex_article")
@bibtex_type("article")
public class BibTeXArticle extends AbstractBibTeXEntry
{

	// required fields

	@simpl_scalar
	@bibtex_tag("journal")
	private String	journal;

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
