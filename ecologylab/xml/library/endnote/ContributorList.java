/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.library.endnote;

import java.util.ArrayList;

import ecologylab.xml.ElementState;
/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class ContributorList extends ElementState
{
	@xml_collection("Author")
	@xml_nowrap 
    ArrayList<Author> authors = new ArrayList<Author>();
    
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
    public ArrayList<Author> getAuthors()
    {
        return authors;
    }

    public boolean contains(String value)
    {
        for (Author a : authors)
        {
            if (a.getTextNodeString().toLowerCase().contains(value))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @return	Concatenation of authors.
     */
    public String getAuthorListString()
    {
        StringBuilder string = new StringBuilder();
        
        int i = authors.size();
        
        for (Author a : authors)
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
