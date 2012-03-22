package ecologylab.appframework.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_map;
import ecologylab.serialization.annotations.simpl_nowrap;

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