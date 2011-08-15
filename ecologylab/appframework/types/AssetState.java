/**
 * 
 */
package ecologylab.appframework.types;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * @author robinson
 *
 */
public class AssetState extends ElementState 
{
	@simpl_scalar	String	id;
	@simpl_scalar	float	version;
	
	public AssetState()
	{
		
	}
	public AssetState(String id)
	{
		this.id		= id;
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
	public String getId()
	{
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id)
	{
		this.id = id;
	}
}
