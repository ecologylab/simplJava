package ecologylab.serialization.deserializers.parsers.bibtex.entrytypes;

import simpl.annotations.dbal.bibtex_tag;
import simpl.annotations.dbal.bibtex_type;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;

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
