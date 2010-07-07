/**
 * 
 */
package ecologylab.xml.library.jnlp.application;

import java.util.ArrayList;

import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.simpl_nowrap;
import ecologylab.xml.ElementState.xml_tag;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public @xml_tag("application-desc") class ApplicationDesc extends ElementState
{
	@simpl_scalar @xml_tag("main-class") String		mainClass;

	@simpl_nowrap 
	@simpl_collection("argument") ArrayList<String>	arguments	= new ArrayList<String>();

	/**
	 * 
	 */
	public ApplicationDesc()
	{
		super();
	}

	/**
	 * Add the argument to the JNLP application description.
	 * 
	 * @param argument
	 */
	public void add(String argument)
	{
		this.arguments.add(argument);
	}

	public ArrayList<String> getArguments()
	{
		return arguments;
	}

}
