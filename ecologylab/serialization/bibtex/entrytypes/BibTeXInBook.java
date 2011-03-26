package ecologylab.serialization.bibtex.entrytypes;

import java.util.ArrayList;

import ecologylab.serialization.ElementState.bibtex_type;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

@simpl_inherit
@xml_tag("bibtex_inbook")
@bibtex_type("inbook")
public class BibTeXInBook extends AbstractBibTeXEntry
{

	// required fields

	@simpl_collection("editor")
	@bibtex_tag("editor")
	private ArrayList<String>	editors;

	@simpl_scalar
	@bibtex_tag("chapter")
	private int								chapter;

	@simpl_scalar
	@bibtex_tag("pages")
	private String						pages;

	@simpl_scalar
	@bibtex_tag("publisher")
	private String						publisher;

	@simpl_scalar
	@bibtex_tag("volume")
	private String						volume;

	@simpl_scalar
	@bibtex_tag("series")
	private String						series;

	@simpl_scalar
	@bibtex_tag("address")
	private String						address;

	@simpl_scalar
	@bibtex_tag("edition")
	private String						edition;

	public ArrayList<String> getEditors()
	{
		return editors;
	}

	public void setEditors(ArrayList<String> editors)
	{
		this.editors = editors;
	}

	public int getChapter()
	{
		return chapter;
	}

	public void setChapter(int chapter)
	{
		this.chapter = chapter;
	}

	public String getPages()
	{
		return pages;
	}

	public void setPages(String pages)
	{
		this.pages = pages;
	}

	public String getPublisher()
	{
		return publisher;
	}

	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}

	public String getVolume()
	{
		return volume;
	}

	public void setVolume(String volume)
	{
		this.volume = volume;
	}

	public String getSeries()
	{
		return series;
	}

	public void setSeries(String series)
	{
		this.series = series;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getEdition()
	{
		return edition;
	}

	public void setEdition(String edition)
	{
		this.edition = edition;
	}

}
