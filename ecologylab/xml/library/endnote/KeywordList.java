/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.endnote;

import ecologylab.xml.ArrayListState;
import ecologylab.xml.XmlTranslationException;
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
            if (k.getTextNodeString() != null && k.getTextNodeString().toLowerCase().contains(value))
            {
                return true;
            }
        }
        
        return false;
    }

    /**
     * @return
     */
    public String getKeywordListString()
    {
        StringBuilder string = new StringBuilder();
        
        int i = this.size();
        
        for (Keyword a : this)
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
