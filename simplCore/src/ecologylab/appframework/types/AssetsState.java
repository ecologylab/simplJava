package ecologylab.appframework.types;

import java.util.Collection;
import java.util.HashMap;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_map;
import simpl.annotations.dbal.simpl_nowrap;

import ecologylab.serialization.ElementState;

/**
 * Handles the loading and parsing of the asset version XML file
 *  
 * @author robinson
 * @author andruid
 */
@simpl_inherit public class AssetsState extends ElementState
{
	@simpl_map("asset")
	@simpl_nowrap
	HashMap<String, AssetState>	assetsMap	= new HashMap<String, AssetState>();
	
	public Collection<AssetState> getAssetStates() 
	{
		return assetsMap.values();
	}

	/**
	 * @param asset
	 */
	private void register(AssetState asset)
	{
		assetsMap.put(asset.getName(), asset);
	}
	
	public AssetState lookup(String name)
	{
		return assetsMap.get(name);
	}
	
	public AssetState lookupAndUpdate(String name)
	{
		AssetState asset = lookup(name);
		if (asset == null)
		{
			asset = update(name);
		}
		return asset;
	}

	/**
	 * @return
	 */
	public AssetState update(String name)
	{
		AssetState asset	= new AssetState(name);
		register(asset);
		return asset;
	}
}