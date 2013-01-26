/**
 * 
 */
package ecologylab.serialization.library.jnlp.resource;

import java.util.ArrayList;

import simpl.annotations.dbal.simpl_classes;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ElementState;


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
