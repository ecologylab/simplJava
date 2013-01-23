package ecologylab.serialization.deserializers.parsers.bibtex.entrytypes;

import ecologylab.serialization.annotations.bibtex_tag;
import ecologylab.serialization.annotations.bibtex_type;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

@simpl_inherit
@simpl_tag("bibtex_techreport")
@bibtex_type("techreport")
public class BibTeXTechReport extends AbstractBibTeXEntry
{

	// required fields

	@simpl_scalar
	@bibtex_tag("institution")
	private String	institution;

	@simpl_scalar
	@bibtex_tag("type")
	private String	type;

	@simpl_scalar
	@bibtex_tag("number")
	private long		number;

	@simpl_scalar
	@bibtex_tag("address")
	private String	address;

	public String getInstitution()
	{
		return institution;
	}

	public void setInstitution(String institution)
	{
		this.institution = institution;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public long getNumber()
	{
		return number;
	}

	public void setNumber(long number)
	{
		this.number = number;
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
