package ecologylab.oodss.messages;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import ecologylab.collections.Scope;

/**
 * Base class for all ResponseMessages that indicate errors.
 * 
 * @author andruid, Zachary O. Toups (zach@ecologylab.net)
 */
@simpl_inherit public class ExplanationResponse<S extends Scope> extends
		ResponseMessage<S>
{
	@simpl_scalar protected String	explanation;

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
