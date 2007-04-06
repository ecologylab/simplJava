package ecologylab.services.messages.cf;

import ecologylab.xml.ElementState;

/**
 * Specification of a directive to the agent or otherwise to compositon space services.
 * 
 * Version for client only:
 * 	<li>data slot definitions only with no other functionality.</li>
 * 
 * @author andruid
 */
public class Seed extends ElementState
{
	@xml_attribute protected float		bias		= 1.0f;

	public Seed()
	{
		super();
	}

	/**
	 * @return Returns the bias.
	 */
	public float getBias()
	{
		return bias;
	}

	/**
	 * @param bias The bias to set.
	 */
	public void setBias(float bias)
	{
		this.bias = bias;
	}

}
