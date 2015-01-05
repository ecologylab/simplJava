/**
 * 
 */
package ecologylab.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * Mostly extends HashMap<String, PrefixPhrase>
 * 
 * However, for put, checks to see if String key is WILDCARD. 
 * If so, sets special wildcardMatch slot instead of adding to HashMap.
 * <p/>
 * Likewise, for get(), if a specific match does not work, chekcs to see if there is a wildcardMatch. 
 * If there is one, it will be returned.
 * 
 * @author andruid
 *
 */
public class ChildPrefixMap extends HashMap<String, PrefixPhrase>
{
	private PrefixPhrase			wildcardMatch;
	
	public static final String	WILDCARD	= "*";

	/**
	 * 
	 */
	public ChildPrefixMap()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param initialCapacity
	 */
	public ChildPrefixMap(int initialCapacity)
	{
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param m
	 */
	public ChildPrefixMap(Map<? extends String, ? extends PrefixPhrase> m)
	{
		super(m);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public ChildPrefixMap(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
		// TODO Auto-generated constructor stub
	}
	/**
 * For put, checks to see if String key is WILDCARD. 
 * If so, sets special wildcardMatch slot instead of adding to HashMap.
	 */
	@Override
  public PrefixPhrase put(String key, PrefixPhrase value) 
	{
		if (WILDCARD.equals(key))
		{
			PrefixPhrase result	= wildcardMatch;
			wildcardMatch				= value;
			return result;
		}
		return super.put(key, value);
	}
	/**
	 * For get(), if a specific match does not work, check to see if there is a wildcardMatch. 
	 * If there is one, it will be returned.	 
	 */
	@Override
  public PrefixPhrase get(Object key) 
	{
		PrefixPhrase result = super.get(key);
		return (result != null) ? result : wildcardMatch;
	}
	
	public PrefixPhrase getWildcardMatch()
	{
		return wildcardMatch;
	}

	public void setWildcardMatch(PrefixPhrase wildcardMatch)
	{
		this.wildcardMatch = wildcardMatch;
	}

	@Override
	public int size()
	{
		int result = super.size();
		if (wildcardMatch != null)
			result++;
		return result;
	}
}
