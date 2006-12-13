/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.endnote;

import ecologylab.xml.ArrayListState;
import ecologylab.xml.xml_inherit;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public @xml_inherit class KeywordList extends ArrayListState<Keyword>
{
    
    
    /**
     * 
     */
    public KeywordList()
    {
        // TODO Auto-generated constructor stub
    }

    public boolean containsString(String value)
    {
        for (Keyword k : this)
        {
            if (k.getString().toLowerCase().contains(value))
            {
                return true;
            }
        }
        
        return false;
    }

}
