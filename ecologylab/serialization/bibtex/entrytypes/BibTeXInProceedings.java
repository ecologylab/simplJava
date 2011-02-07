package ecologylab.serialization.bibtex.entrytypes;

import java.util.ArrayList;

import ecologylab.serialization.ElementState.bibtex_type;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

@simpl_inherit
@xml_tag("bibtex_inproceedings")
@bibtex_type("inproceedings")
public class BibTeXInProceedings extends AbstractBibTeXEntry
{

	// required fields

	@simpl_scalar
	@bibtex_tag("booktitle")
	private String						booktitle;

	@simpl_scalar
	@bibtex_tag("year")
	private int								year;

	// optional fields

	@simpl_collection("editor")
	@bibtex_tag("editor")
	private ArrayList<String>	editors;

	@simpl_scalar
	@bibtex_tag("pages")
	private String						pages;

	@simpl_scalar
	@bibtex_tag("organization")
	private int								organization;

	@simpl_scalar
	@bibtex_tag("publisher")
	private String						publisher;

	@simpl_scalar
	@bibtex_tag("address")
	private String						address;

	public String getBooktitle()
	{
		return booktitle;
	}

	public void setBooktitle(String booktitle)
	{
		this.booktitle = booktitle;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public ArrayList<String> getEditors()
	{
		return editors;
	}

	public void setEditors(ArrayList<String> editors)
	{
		this.editors = editors;
	}

	public String getPages()
	{
		return pages;
	}

	public void setPages(String pages)
	{
		this.pages = pages;
	}

	public int getOrganization()
	{
		return organization;
	}

	public void setOrganization(int organization)
	{
		this.organization = organization;
	}

	public String getPublisher()
	{
		return publisher;
	}

	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

}
