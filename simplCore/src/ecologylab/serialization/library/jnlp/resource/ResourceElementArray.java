/**
 * 
 */
package ecologylab.serialization.library.jnlp.resource;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * The resources element is used to specify all the resources, such as Java class files, native
 * libraries, and system properties, that are part of the application. A resource definition can be
 * restricted to a specific operating system, architecture, or locale using the os, arch, and locale
 * attributes.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class ResourceElementArray extends ElementState
{
	@simpl_scalar
	private String							os;

	@simpl_scalar
	private String							arch;

	@simpl_scalar
	private String							locale;

	@simpl_collection
	@simpl_nowrap
	@simpl_classes(
	{ ResourceElement.class, HrefBasedResource.class, J2se.class, Jar.class })
	ArrayList<ResourceElement>	resourceElements;

	/**
     * 
     */
	public ResourceElementArray()
	{
		super();
	}

}
