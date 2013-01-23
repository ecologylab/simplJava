/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class ContributorList extends ElementState
{
	@simpl_collection("Author")
	@simpl_nowrap 
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
        	//FIXME -- need to code a newer version of s.im.pl serialization
//            if (a.getTextNodeString().toLowerCase().contains(value))
//            {
//                return true;
//            }
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
        	//FIXME -- need to code a newer version of s.im.pl serialization
//            i--;
//            string.append(a.getTextNodeString());
//            
//            if (i != 0)
//            {
//                string.append("; ");
//            }
        }
        
        return string.toString();
    }

}
