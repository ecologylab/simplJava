package ecologylab.services.messages;

import ecologylab.collections.Scope;
import ecologylab.xml.xml_inherit;

/**
 * Base class for all ResponseMessages that indicate errors.
 * 
 * @author andruid, Zachary O. Toups (zach@ecologylab.net)
 */
@xml_inherit public class ExplanationResponse<S extends Scope> extends
		ResponseMessage<S>
{
	@xml_attribute protected String	explanation;

	public ExplanationResponse()
	{
		super();
	}

	public ExplanationResponse(String explanation)
	{
		this();
		this.explanation = explanation;
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
	 * @param explanation
	 *           the explanation to set
	 */
	public void setExplanation(String explanation)
	{
		this.explanation = explanation;
	}

}
