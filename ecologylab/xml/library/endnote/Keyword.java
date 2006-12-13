/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.endnote;

import ecologylab.xml.StringState;
import ecologylab.xml.xml_inherit;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public @xml_inherit class Keyword extends StringState
{

    /**
     * 
     */
    public Keyword()
    {
    }

    /**
     * @param string
     */
    public Keyword(String string)
    {
        super(string);
    }

}
