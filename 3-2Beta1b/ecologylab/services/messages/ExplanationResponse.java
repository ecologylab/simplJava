package ecologylab.services.messages;

import ecologylab.xml.xml_inherit;

/**
 * Base class for all ResponseMessages that indicate errors.
 * 
 * @author andruid, Zachary O. Toups (toupsz@cs.tamu.edu)
 */
@xml_inherit
public class ExplanationResponse extends ResponseMessage
{
	@xml_attribute protected String explanation;
	
	public ExplanationResponse()
	{
		super();
	}

	public ExplanationResponse(String explanation)
	{
		this();
		this.explanation	= explanation;
	}

	@Override public boolean isOK()
	{
		return true;
	}

    /**
     * @return Returns the explanation.
     */
    public String getExplanation()
    {
        return explanation;
    }

	/**
	 * @param explanation the explanation to set
	 */
	public void setExplanation(String explanation)
	{
		this.explanation = explanation;
	}

}
