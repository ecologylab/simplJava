/**
 * 
 */
package ecologylab.collections;

import java.util.ArrayList;
import java.util.HashMap;

import ecologylab.generic.Debug;
import ecologylab.generic.HashMapWriteSynch;
import ecologylab.generic.ValueFactory;

/**
 * Recursive unit (bucket) for prefix pattern matching.
 * 
 * @author andruid
 *
 */
class PrefixPhrase extends Debug
implements ValueFactory<String, PrefixPhrase>
{
	final	String			phrase;
	
	final	PrefixPhrase	parent;
	
	HashMapWriteSynch<String, PrefixPhrase>	childPhraseMap	= new HashMapWriteSynch<String, PrefixPhrase>();

	/**
	 * 
	 */
	public PrefixPhrase(PrefixPhrase parent, String phrase)
	{
		this.parent		= parent;
		this.phrase		= phrase;
	}

	public PrefixPhrase add(PrefixPhrase parent, String string, char separator)
	{
		return add(parent, string, 0, separator);
	}
	
	protected PrefixPhrase add(PrefixPhrase parent, String string, int start, char separator)
	{
		int end				= string.length();
		if (start == end)
			return null;
		
		PrefixPhrase result	= null;
		int nextSeparator	= string.indexOf(separator, start);
		if (nextSeparator == -1)
			nextSeparator	= end;
		
		if (nextSeparator > -1)
		{
			String phraseString	= string.substring(start, nextSeparator);
			// extra round of messing with synch, because we need to know if we
			// are creating a new Phrase
			PrefixPhrase nextPrefixPhrase	= getPrefix(this, phraseString);
			if (nextPrefixPhrase == null)
			{
				// done!
				return null;
//				synchronized (this)
//				{
//					nextPrefixPhrase	= getPrefix(this, phraseString);
//					if (nextPrefixPhrase == null)
//					{
//						nextPrefixPhrase	= childPhraseMap.getOrCreateAndPutIfNew(phraseString, this);
//						result				= nextPrefixPhrase;
//					}
//				}
			}
			else
			{
				PrefixPhrase recursion			= add(this, string, nextSeparator, separator);
				return (result == null) ? recursion : nextPrefixPhrase;
			}
		}
		else
		{
			Debug.println("help! wrong block!!!");
			// last segment
			if (start == string.length() - 1)
			{
				
			}
			else
			{
				
			}
		}
		return result;
	}
	

	/**
	 * Seek the PrefixPhrase corresponding to the argument.
	 * If it does not exist, return it.
	 * <p/>
	 * If it does exist, does it have 0 children?
	 * 		If so, return null. No need to insert for the argument's phrase.
	 * 		If not, return it.
	 * 
	 * @param prefixPhrase
	 * @return
	 */
	protected PrefixPhrase getPrefix(PrefixPhrase parent, String prefixPhrase)
	{
		PrefixPhrase domainPrefix	= childPhraseMap.get(prefixPhrase);
		
		if (domainPrefix == null)
		{
			domainPrefix	= new PrefixPhrase(parent, prefixPhrase);
			childPhraseMap.put(prefixPhrase, domainPrefix);
		}
		else
		{
			if (domainPrefix.isTerminal())
				return null;
		}
		
		return domainPrefix;
	}


	void toBuffy(StringBuilder buffy, char separator)
	{
		if (parent != null)
		{
			parent.toBuffy(buffy, separator);
			buffy.append(separator);
		}
		buffy.append(phrase);
	}
	public PrefixPhrase createValue(String phrase)
	{
		return new PrefixPhrase(this, phrase);
	}
	
	public int numChildren()
	{
		return childPhraseMap.size();
	}
	
	/**
	 * Is the end of a prefix.
	 * 
	 * @return
	 */
	public boolean isTerminal()
	{
		return numChildren() == 0;
	}
}
