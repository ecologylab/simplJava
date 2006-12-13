/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.endnote;

import ecologylab.xml.StringState;
import ecologylab.xml.xml_inherit;

public @xml_inherit class Author extends StringState
{
    public Author()
    {
        
    }

    public Author(String name)
    {
        this.string = name;
    }
}
