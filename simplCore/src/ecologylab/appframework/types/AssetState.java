/**
 * 
 */
package ecologylab.appframework.types;

import simpl.annotations.dbal.simpl_scalar;
import simpl.types.element.IMappable;
import ecologylab.serialization.ElementState;

/**
 * @author robinson
 *
 */
public class AssetState extends ElementState implements IMappable<String>
{
	@simpl_scalar	String	name;
	@simpl_scalar	float	version;
	
	public AssetState()
	{
		
	}
	public AssetState(String name)
	{
		this.name		= name;
	}
	/**
	 * @return Returns the version.
	 */
	public float getVersion()
	{
		return version;
	}
	
	/**
	 * @param version The version to set.
	 */
	public void setVersion(float version)
	{
		this.version = version;
	}
	
	/**
	 * @return Returns the id.
	 */
	public String getName()
	{
		return name;
	}

	@Override
	public String key()
	{
		return name;
	}
}
