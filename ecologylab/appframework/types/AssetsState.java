package ecologylab.appframework.types;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.appframework.StatusReporter;
import ecologylab.generic.DispatchTarget;
import ecologylab.generic.Generic;
import ecologylab.io.Assets;
import ecologylab.io.Files;
import ecologylab.xml.ElementState;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * Handles the loading and parsing of the asset version XML file
 *  
 * @author robinson
 */
@xml_inherit public class AssetsState extends ArrayListState<AssetState>
{
	HashMap<String, AssetState>	assetsMap	= new HashMap<String, AssetState>();
	
	/**
	 * Perform custom processing on the newly created child node,
	 * just before it is added to this.
	 * <p/>
	 * This is part of depth-first traversal during translateFromXML().
	 * <p/>
	 * This, the default implementation, does nothing.
	 * Sub-classes may wish to override.
	 * 
	 * @param child
	 */
	protected void createChildHook(ElementState child)
	{
		AssetState asset	= (AssetState) child;
		register(asset);
	}

	/**
	 * @param asset
	 */
	private void register(AssetState asset)
	{
		assetsMap.put(asset.getId(), asset);
	}
	
	public AssetState lookup(String id)
	{
		return assetsMap.get(id);
	}
	public AssetState lookupAndUpdate(String id)
	{
		AssetState asset = lookup(id);
		if (asset == null)
		{
			asset = update(id);
		}
		return asset;
	}

	/**
	 * @return
	 */
	public AssetState update(String id)
	{
		AssetState asset	= new AssetState(id);
		add(asset);
		register(asset);
		return asset;
	}

	public boolean contains(String id)
	{
		HashMap<String, AssetState> hashMap = assetsMap;
		return hashMap.containsKey(id);
	}
}