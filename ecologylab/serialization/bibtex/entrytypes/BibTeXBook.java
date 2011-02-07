package ecologylab.serialization.bibtex.entrytypes;

import java.util.ArrayList;

import ecologylab.serialization.ElementState.bibtex_type;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

@simpl_inherit
@xml_tag("bibtex_book")
@bibtex_type("book")
public class BibTeXBook extends AbstractBibTeXEntry
{

	// required fields

	@simpl_collection("editor")
	@bibtex_tag("editor")
	private ArrayList<String>	editors;

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

	public String getPublisher()
	{
		return publisher;
	}

	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}

	public ArrayList<String> getEditors()
	{
		return editors;
	}

	public void setEditors(ArrayList<String> editors)
	{
		this.editors = editors;
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
