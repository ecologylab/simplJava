/**
 * The Assets class is used to manage cachable assets.
 */
package ecologylab.generic.AssetsCache;

import java.io.File;

/**
 * Used to manage cachable assets
 * 
 * @author blake
 */
public class Assets 
{
	static File cacheRoot = null;
	
	/**
	 * current known assets (although arbitrary ones can exist)
	 */
	public static final String INTERFACE		= "interface";
	public static final String SEMANTICS		= "semantics";
	
	/**
	 * No instances possible, static references only.
	 */
	private Assets() {}
	
	/**
	 * Sets the root file path for caching. Assets are specified relative to this path.
	 * @param cacheRoot The root file path for caching assets.
	 */
	public static void setCacheRoot(File cacheRoot)
	{
		Assets.cacheRoot = cacheRoot;
	}
	
	/**
	 * Given a relative path, return a file reference to this path
	 * from the cache root.
	 * 
	 * @param relativePath	A string representing the relative file path.
	 * @return	A file reference to the requested path
	 */
	public static File getAsset(String relativePath)
	{
		if (cacheRoot == null)
			return null;
		
		return new File(cacheRoot.getAbsolutePath() + File.separatorChar + relativePath);
	}
	
	public static File getAndPerhapsCreateAsset(String relativePath)
	{
		File theAsset = getAsset(relativePath);
		
		if (!theAsset.exists())
		{
			File currentAsset = theAsset;
			while (currentAsset != null && !currentAsset.exists())
			{
				currentAsset.mkdir();
				currentAsset = currentAsset.getParentFile();
			}
		}
		
		return theAsset;
	}
	
}
