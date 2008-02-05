/**
 * 
 */
package ecologylab.collections;

import java.util.ArrayList;
import java.util.HashMap;

import ecologylab.generic.Debug;
import ecologylab.generic.HashMapArrayList;
import ecologylab.generic.HashMapWriteSynch;
import ecologylab.generic.ValueFactory;

/**
 * Recursive unit (bucket) for prefix pattern matching.
 * 
 * @author andruid
 *
 */
class PrefixPhrase extends Debug
{
	final	String			phrase;
	
	final	PrefixPhrase	parent;
	
	HashMapArrayList<String, PrefixPhrase>	childPhraseMap	= new HashMapArrayList<String, PrefixPhrase>();

	/**
	 * 
	 */
	public PrefixPhrase(PrefixPhrase parent, String phrase)
	{
		this.parent		= parent;
		this.phrase		= phrase;
	}

	public PrefixPhrase add(String string, char separator)
	{
		return add(string, 0, separator);
	}
	
	protected PrefixPhrase add(String string, int start, char separator)
	{
		int end				= string.length();
		boolean terminate	= false;
		
		if (start == end)
			terminate		= true;
		else
		{		
			if (string.charAt(start) == separator)
				start++;
			if (start == end)
				terminate	= true;
		}
		if (terminate)
		{
			clear();
			return this;
		}
		int nextSeparator	= string.indexOf(separator, start);
		if (nextSeparator == -1)
			nextSeparator	= end;
		
		if (nextSeparator > -1)
		{
			String phraseString	= string.substring(start, nextSeparator);
			// extra round of messing with synch, because we need to know if we
			// are creating a new Phrase
			PrefixPhrase nextPrefixPhrase	= getPrefix(this, phraseString);
			if (nextPrefixPhrase != null)
			{
				return nextPrefixPhrase.add(string, nextSeparator, separator);
			}
			else
			{
				// done!
				PrefixPhrase newTerminal	= lookupChild(phraseString);
				
				newTerminal.clear();
				return newTerminal;
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
		}
		else
		{
			Debug.println("help! wrong block!!!");
			// last segment
			return null;
		}
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

	protected PrefixPhrase lookupChild(String prefix)
	{
		return childPhraseMap.get(prefix);
	}
	
	protected void clear()
	{
		childPhraseMap.clear();
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
