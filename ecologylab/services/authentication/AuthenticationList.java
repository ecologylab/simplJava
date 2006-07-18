/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.util.HashMap;

import ecologylab.xml.ElementState;
import ecologylab.xml.XmlTranslationException;

/**
 * Contains a HashMap of AuthenticationListEntry's that are hashed on their
 * username values.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class AuthenticationList extends ElementState
{

    public HashMap authList = new HashMap();

    public AuthenticationList()
    {
        super();
    }

    /**
     * @override makes sure that the reconstructed AuthenticationList is hashed
     *           on the username of the entries.
     */
    public void addNestedElement(ElementState elementState)
    {
        authList.put(((AuthenticationListEntry) elementState).getUsername(),
                (AuthenticationListEntry) elementState);
    }

    /**
     * Adds an entry to the authentication list.
     * 
     * @param entry -
     *            the list entry to add to the list.
     */
    public void add(AuthenticationListEntry entry)
    {
        authList.put(entry.getUsername(), entry);
    }

    /**
     * Retrieves the list entry whose name matches the argument, or null if none
     * exists.
     * 
     * @param username -
     *            the username to look up.
     * @return the entry whose username matches the argument; null if it does
     *         not exist.
     */
    public AuthenticationListEntry get(String username)
    {
        return (AuthenticationListEntry) authList.get(username.toLowerCase());
    }

    /**
     * Checks to see if the username already exists in the list.
     * 
     * @param username -
     *            the username to look up.
     * @return true if the entry exists, false otherwise.
     */
    public boolean containsKey(String username)
    {
        return authList.containsKey(username.toLowerCase());
    }

}
