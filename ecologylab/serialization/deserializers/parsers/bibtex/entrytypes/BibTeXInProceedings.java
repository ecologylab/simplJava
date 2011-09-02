package ecologylab.serialization.deserializers.parsers.bibtex.entrytypes;

import java.util.ArrayList;

import ecologylab.serialization.annotations.bibtex_tag;
import ecologylab.serialization.annotations.bibtex_type;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

@simpl_inherit
@simpl_tag("bibtex_inproceedings")
@bibtex_type("inproceedings")
public class BibTeXInProceedings extends AbstractBibTeXEntry
{

	// required fields

	@simpl_scalar
	@bibtex_tag("booktitle")
	private String						booktitle;

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
