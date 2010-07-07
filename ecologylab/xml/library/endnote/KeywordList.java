/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.library.endnote;

import java.util.ArrayList;

import ecologylab.xml.ElementState;
import ecologylab.xml.simpl_inherit;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public @simpl_inherit class KeywordList extends ElementState
{
    @simpl_collection("Keyword")
    @simpl_nowrap
    ArrayList<Keyword> keywords;
    
    /**
     * 
     */
    public KeywordList()
    {
        // TODO Auto-generated constructor stub
    }

    public boolean containsString(String value)
    {
        for (Keyword k : keywords)
        {
            if (k.getTextNodeString() != null && k.getTextNodeString().toLowerCase().contains(value))
            {
                return true;
            }
        }
        
        return false;
    }

    /**
     * @return	list of keywords associated with this.
     */
    public String getKeywordListString()
    {
        StringBuilder string = new StringBuilder();
        
        int i = keywords.size();
        
        for (Keyword a : keywords)
        {
            i--;
            string.append(a.getTextNodeString());
            
            if (i != 0)
            {
                string.append("; ");
            }
        }
        
        return string.toString();
    }
}
