/**
 * 
 */
package ecologylab.collections;

import java.util.ArrayList;

import ecologylab.generic.Debug;
import ecologylab.generic.HashMapWriteSynch;
import ecologylab.generic.ValueFactory;

/**
 * Recursive unit (bucket) for prefix pattern matching.
 * 
 * @author andruid
 *
 */
class PrefixPhrase extends HashMapWriteSynch<String, PrefixPhrase>
implements ValueFactory<String, PrefixPhrase>
{
	final	String			phrase;
	
	final	PrefixPhrase	parent;
	
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
			PrefixPhrase nextPrefixPhrase	= get(phraseString);
			if (nextPrefixPhrase == null)
			{
				synchronized (this)
				{
					nextPrefixPhrase	= get(phraseString);
					if (nextPrefixPhrase == null)
					{
						nextPrefixPhrase	= super.getOrCreateAndPutIfNew(phraseString, this);
						result				= nextPrefixPhrase;
					}
				}
			}
			PrefixPhrase recursion			= add(string, nextSeparator, separator);
			return (result == null) ? recursion : nextPrefixPhrase;
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

	void formString(StringBuilder buffy)
	{
		if (parent != null)
			parent.formString(buffy);
		buffy.append(phrase);
	}
	public PrefixPhrase createValue(String phrase)
	{
		return new PrefixPhrase(this, phrase);
	}
}
