/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.util.Collection;
import java.util.HashMap;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.subelements.ArrayListState;

/**
 * Contains a HashMap of AuthenticationListEntry's that are hashed on their
 * username values.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public @xml_inherit class AuthenticationList extends
        ElementState
{
    @xml_nested private ArrayListState<AuthenticationListEntry> authList = new ArrayListState<AuthenticationListEntry>();
    private HashMap<String, AuthenticationListEntry> nameToEntryMap = null;

    public AuthenticationList()
    {
        super();
    }

    /**
     * Adds the given entry to this.
     */
    public boolean add(AuthenticationListEntry entry)
    {
        if (!this.nameToEntryMap().containsKey(entry.getUsername()))
        {
            authList.add(entry);
            nameToEntryMap().put(entry.getUsername(), entry);

            return true;
        }

        return false;
    }

    /**
     * Attempts to add each element of c to this. If any of the elements of c
     * are not
     */
    public boolean addAll(Collection c) throws ClassCastException
    {
        for (Object o : c)
        {
            if (!(o instanceof AuthenticationListEntry))
            {
                throw new ClassCastException(
                        "At least one element in the Collection was not an AuthenticationListEntry; no entries added.");
            }
        }

        for (Object e : c)
        {
            this.add((AuthenticationListEntry) e);
        }

        return true;
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#clone()
     */
    @Override public Object clone() throws UnsupportedOperationException
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
        return nameToEntryMap().containsKey(username);
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection c)
            throws UnsupportedOperationException
    {
        if (c == null)
            throw new NullPointerException("Collection was null.");

        for (Object o : c)
        {
            if (o == null)
                throw new NullPointerException(
                        "At least one of the entries was null; this not supported.");
            else if (!(o instanceof AuthenticationListEntry))
                throw new ClassCastException(
                        "At least one of the entries was not an AuthenticationListEntry.");
        }

        for (Object o : c)
        {
            if (!this.contains((AuthenticationListEntry) o))
                return false;
        }

        return true;
    }

    /**
     * Retrieves the access level for the given entry.
     * 
     * @param entry
     * @return
     */
    public int getAccessLevel(AuthenticationListEntry entry)
    {
        return nameToEntryMap().get(entry.getUsername()).getLevel();
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
        return (nameToEntryMap().containsKey(entry.getUsername()) && nameToEntryMap.get(
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
            return authList.remove(nameToEntryMap.remove(((AuthenticationListEntry) o)
                    .getUsername()));
        }

        return false;
    }
    
    public String toString()
    {
        return "AuthenticationList containing " + authList.size() + " entries.";
    }

    /**
     * Constructs the internal HashMap to improve retrievals.
     * 
     * Note that this method also ensures that the passwords are hashed properly
     * (which would normally be done with the constructor). They should
     * (currently) be plaintext in the XML file.
     * 
     * TODO Make it so that passwords stored in the file are already hashed;
     * this will require a small command line program!!!
     * 
     * @return
     */
    private HashMap<String, AuthenticationListEntry> nameToEntryMap()
    {
        if (nameToEntryMap == null)
        {
            nameToEntryMap = new HashMap<String, AuthenticationListEntry>(authList.size());

            for (AuthenticationListEntry e : authList)
            {
                e.setAndHashPassword(e.getPassword());

                nameToEntryMap.put(e.getUsername(), e);
            }
        }

        return nameToEntryMap;
    }
}
