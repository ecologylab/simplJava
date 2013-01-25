/**
 * 
 */
package ecologylab.serialization.library.jnlp.applet;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class AppletDesc extends ElementState
{
	@simpl_scalar
	private String		documentBase;

	@simpl_scalar
	private String		name;

	@simpl_scalar
	@simpl_tag("main-class")
	private String		mainClass;

	@simpl_scalar
	private int				width;

	@simpl_scalar
	private int				height;

	@simpl_nowrap
	@simpl_collection("Param")
	ArrayList<Param>	params;

	/**
     * 
     */
	public AppletDesc()
	{
		super();
	}

}
