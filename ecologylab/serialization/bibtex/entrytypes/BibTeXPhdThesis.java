package ecologylab.serialization.bibtex.entrytypes;

import ecologylab.serialization.ElementState.bibtex_type;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

@simpl_inherit
@xml_tag("bibtex_phdthesis")
@bibtex_type("phdthesis")
public class BibTeXPhdThesis extends AbstractBibTeXEntry
{

	// required fields

	@simpl_scalar
	@bibtex_tag("school")
	private String	school;

	@simpl_scalar
	@bibtex_tag("year")
	private int			year;

	// optional fields

	@simpl_scalar
	@bibtex_tag("address")
	private String	address;

	public String getSchool()
	{
		return school;
	}

	public void setSchool(String school)
	{
		this.school = school;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
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
