package ecologylab.services.messages;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;

/**
 * Base class for all ResponseMessages that indicate errors.
 * 
 * @author andruid
 */
@xml_inherit
public class ErrorResponse extends ResponseMessage
{
	@xml_attribute protected String explanation;
	
	public ErrorResponse()
	{
		super();
	}

	public ErrorResponse(String response)
	{
		this();
		this.explanation	= response;
	}

	public boolean isOK()
	{
		return false;
	}

    /**
     * @return Returns the explanation.
     */
    public String getExplanation()
    {
        return explanation;
    }

}
