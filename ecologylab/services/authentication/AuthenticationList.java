/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import ecologylab.xml.ElementState;

/**
 * Contains a HashMap of AuthenticationListEntry's that are hashed on their
 * username values.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class AuthenticationList extends
        ElementState
{
    @xml_nested private AuthListHashMap authList = new AuthListHashMap();

    public AuthenticationList()
    {
        super();
    }

    /**
     * Adds the given entry to this.
     */
    public boolean add(AuthenticationListEntry entry)
    {
        if (!this.authList.containsKey(entry.getUsername()))
        {
            authList.put(entry.getUsername(), entry);

            return true;
        }

        return false;
    }

    /**
     * @see ecologylab.xml.types.element.ArrayListState#clone()
     */
    @Override public final Object clone() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "Cannot clone an AuthenticationList.");
    }

    /**
     * Checks to see if this contains the username given in entry; returns true
     * if it does.
     * 
     * @param entry
     * @return
     */
    public boolean contains(AuthenticationListEntry entry)
    {
        System.out.println("ASDF_-----------------------------");
        for (String e : authList.keySet())
        {
            System.out.println(e);
        }
        return this.contains(entry.getUsername());
    }

    /**
     * Checks to see if this contains the given username; returns true if it
     * does.
     * 
     * @param username
     * @return
     */
    public boolean contains(String username)
    {
        return authList.containsKey(username);
    }

    /**
     * Retrieves the access level for the given entry.
     * 
     * @param entry
     * @return
     */
    public int getAccessLevel(AuthenticationListEntry entry)
    {
        return authList.get(entry.getUsername()).getLevel();
    }

    /**
     * Checks entry against the entries contained in this. Verifies that the
     * username exists, and the password matches; returns true if both are true.
     * 
     * @param entry
     * @return
     */
    public boolean isValid(AuthenticationListEntry entry)
    {
        System.out.println("contains key: "+authList.containsKey(entry.getUsername()));
        
        System.out.println(entry.getPassword());
        
        System.out.println(authList.get(entry.getUsername()).getPassword());
        
        return (authList.containsKey(entry.getUsername()) && authList.get(
                entry.getUsername()).compareHashedPassword(entry.getPassword()));
    }

    /**
     * Attempts to remove the given object; this will succeed if and only if the
     * following are true:
     * 
     * 1.) the Object is of type AuthenticationListEntry 2.) this list contains
     * the AuthenticationListEntry 3.) the AuthenticationListEntry's username
     * and password both match the one in this list
     */
    public boolean remove(AuthenticationListEntry o)
    {
        if (this.isValid((AuthenticationListEntry) o))
        {
            return o.equals(authList.remove(o
                    .getUsername()));
        }

        return false;
    }
    
    public String toString()
    {
        return "AuthenticationList containing " + authList.size() + " entries.";
    }
}
