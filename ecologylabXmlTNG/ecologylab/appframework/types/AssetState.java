/**
 * 
 */
package ecologylab.appframework.types;

import ecologylab.xml.ElementState;

/**
 * @author robinson
 *
 */
public class AssetState extends ElementState 
{
	@xml_attribute	String	id;
	@xml_attribute	float	version;
	
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
