package ecologylab.xml.library.rest;

import java.net.URL;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.library.dc.Dc;

@xml_inherit
public class Fields extends Dc
{
	
	@xml_leaf String primaryIdentifier;
	@xml_leaf String category;
	@xml_leaf String compoundAgent;
	@xml_leaf String compoundGenre;
	@xml_leaf String compoundTitle;
	@xml_leaf String compoundDescription;
	@xml_leaf String compoundSubject;
	@xml_leaf String primaryCollection;
	@xml_leaf String brandTitle;
	@xml_leaf URL	 brandIconURL;
	@xml_leaf int	 brandWidth;
	@xml_leaf int	 brandHeight;
	@xml_leaf String bestPassage;
	
	public Fields()
	{
		super();
	}
	
	public String toString()
	{
		return "Fields: {\n" + super.toString() 				+ "\n" +
			"primaryIdentifier: "   + getPrimaryIdentifier() 	+ "\n" + 
			"category: " 			+ category					+ "\n" +
			"compoundAgent: " 		+ compoundAgent				+ "\n" +
			"compoundGenre: " 		+ compoundGenre				+ "\n" +
			"compoundTitle: " 		+ getCompoundTitle()		+ "\n" +
			"compoundDescription: " + getCompoundDescription()	+ "\n" +
			"compoundSubject: " 	+ getCompoundSubject()		+ "\n" +
			"primaryCollection: " 	+ primaryCollection			+ "\n" +
			"brandTitle: " 			+ getBrandTitle()			+ "\n" +
			"brandIconURL: " 		+ getBrandIconURL()			+ "\n" +
			"brandWidth: " 			+ getBrandWidth()			+ "\n" +
			"brandHeight: " 		+ getBrandHeight()			+ "\n" +
			"bestPassage: " 		+ bestPassage				+ "\n" +
			"}";
		
			
	}

	/**
	 * @param compoundTitle the compoundTitle to set
	 */
	public void setCompoundTitle(String compoundTitle)
	{
		this.compoundTitle = compoundTitle;
	}

	/**
	 * @return the compoundTitle
	 */
	public String getCompoundTitle()
	{
		return compoundTitle;
	}

	/**
	 * @param compoundDescription the compoundDescription to set
	 */
	public void setCompoundDescription(String compoundDescription)
	{
		this.compoundDescription = compoundDescription;
	}

	/**
	 * @return the compoundDescription
	 */
	public String getCompoundDescription()
	{
		return compoundDescription;
	}

	/**
	 * @param compoundSubject the compoundSubject to set
	 */
	public void setCompoundSubject(String compoundSubject)
	{
		this.compoundSubject = compoundSubject;
	}

	/**
	 * @return the compoundSubject
	 */
	public String getCompoundSubject()
	{
		return compoundSubject;
	}

	/**
	 * @param brandIconURL the brandIconURL to set
	 */
	public void setBrandIconURL(URL brandIconURL)
	{
		this.brandIconURL = brandIconURL;
	}

	/**
	 * @return the brandIconURL
	 */
	public URL getBrandIconURL()
	{
		return brandIconURL;
	}

	/**
	 * @param brandTitle the brandTitle to set
	 */
	public void setBrandTitle(String brandTitle)
	{
		this.brandTitle = brandTitle;
	}

	/**
	 * @return the brandTitle
	 */
	public String getBrandTitle()
	{
		return brandTitle;
	}

	/**
	 * @param brandWidth the brandWidth to set
	 */
	public void setBrandWidth(int brandWidth)
	{
		this.brandWidth = brandWidth;
	}

	/**
	 * @return the brandWidth
	 */
	public int getBrandWidth()
	{
		return brandWidth;
	}

	/**
	 * @param brandHeight the brandHeight to set
	 */
	public void setBrandHeight(int brandHeight)
	{
		this.brandHeight = brandHeight;
	}

	/**
	 * @return the brandHeight
	 */
	public int getBrandHeight()
	{
		return brandHeight;
	}

	/**
	 * @param primaryIdentifier the primaryIdentifier to set
	 */
	public void setPrimaryIdentifier(String primaryIdentifier)
	{
		this.primaryIdentifier = primaryIdentifier;
	}

	/**
	 * @return the primaryIdentifier
	 */
	public String getPrimaryIdentifier()
	{
		return primaryIdentifier;
	}
}
