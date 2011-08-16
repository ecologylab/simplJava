package ecologylab.serialization.bibtex.entrytypes;

import ecologylab.serialization.annotations.bibtex_tag;
import ecologylab.serialization.annotations.bibtex_type;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

@simpl_inherit
@simpl_tag("bibtex_phdthesis")
@bibtex_type("phdthesis")
public class BibTeXPhdThesis extends AbstractBibTeXEntry
{

	// required fields

	@simpl_scalar
	@bibtex_tag("school")
	private String	school;

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

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

}
