/**
 * 
 */
package ecologylab.appframework.types;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.types.element.IMappable;

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
