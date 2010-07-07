package ecologylab.serialization.library.rest;

import java.net.URL;

import ecologylab.serialization.Hint;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.library.dc.Dc;

@simpl_inherit
public class Fields extends Dc
{
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String primaryIdentifier;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String category;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String compoundAgent;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String compoundGenre;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String compoundTitle;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String compoundDescription;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String compoundSubject;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String primaryCollection;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String brandTitle;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) URL	 brandIconURL;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) int	 brandWidth;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) int	 brandHeight;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String bestPassage;
	
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
