/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.endnote;

import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public class ContributorList extends ElementState
{
    private @xml_nested ArrayListState<Author> authors = new ArrayListState<Author>();
//    private @xml_nested ArrayListState<Author> secondary-authors = new ArrayListState<Author>();
  //  private @xml_nested ArrayListState<Author> tertiary-authors = new ArrayListState<Author>();

    /**
     * 
     */
    public ContributorList()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the authors
     */
    public ArrayListState<Author> getAuthors()
    {
        return authors;
    }

    public boolean contains(String value)
    {
        for (Author a : authors)
        {
            if (a.getString().toLowerCase().contains(value))
            {
                return true;
            }
        }
        return false;
    }

}
