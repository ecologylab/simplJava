/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.subelements.ArrayListState;

/**
 * Contains a HashMap of AuthenticationListEntry's that are hashed on their
 * username values.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public @xml_inherit class AuthenticationList extends
        ArrayListState<AuthenticationListEntry>
{
    private HashMap<String, AuthenticationListEntry> authList = null;

    public AuthenticationList()
    {
        super();
    }

    /**
     * Adds the given entry to this.
     * 
     * @see ecologylab.xml.subelements.ArrayListState#add(ecologylab.xml.ElementState)
     */
    @Override public boolean add(AuthenticationListEntry entry)
    {
        if (!this.authList().containsKey(entry.getUsername()))
        {
            super.add(entry);
            authList().put(entry.getUsername(), entry);

            return true;
        }

        return false;
    }

    /**
     * Attempts to add each element of c to this. If any of the elements of c
     * are not
     * 
     * @see ecologylab.xml.subelements.ArrayListState#addAll(java.util.Collection)
     */
    @Override public boolean addAll(Collection c) throws ClassCastException
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
     * @see ecologylab.xml.subelements.ArrayListState#contains(java.lang.Object)
     */
    @Override public boolean contains(Object o)
    {
        if (o == null)
        {
            throw new NullPointerException("");
        }
        else if (o instanceof AuthenticationListEntry)
        {
            return this.contains((AuthenticationListEntry) o);
        }
        else if (o instanceof String)
        {
            return this.contains((String) o);
        }
        else
        {
            throw new ClassCastException(
                    "Can only check contains on AuthenticationListEntries and Strings");
        }
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
        return authList().containsKey(username);
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#containsAll(java.util.Collection)
     */
    @Override public boolean containsAll(Collection c)
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
        return authList().get(entry.getUsername()).getLevel();
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
        return (authList().containsKey(entry.getUsername()) && authList.get(
                entry.getUsername()).compareHashedPassword(entry.getPassword()));
    }

    /**
     * Attempts to remove the given object; this will succeed if and only if the
     * following are true:
     * 
     * 1.) the Object is of type AuthenticationListEntry 2.) this list contains
     * the AuthenticationListEntry 3.) the AuthenticationListEntry's username
     * and password both match the one in this list
     * 
     * @see ecologylab.xml.subelements.ArrayListState#remove(java.lang.Object)
     */
    @Override public boolean remove(Object o)
    {
        if (!(o instanceof AuthenticationListEntry))
        {
            throw new ClassCastException(
                    "Remove only works with AuthenticationListEntry objects.");
        }

        if (this.isValid((AuthenticationListEntry) o))
        {
            return super.remove(authList.remove(((AuthenticationListEntry) o)
                    .getUsername()));
        }

        return false;
    }
    
    public String toString()
    {
        return "AuthenticationList containing " + this.size() + " entries.";
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
    private HashMap<String, AuthenticationListEntry> authList()
    {
        if (authList == null)
        {
            authList = new HashMap<String, AuthenticationListEntry>(this.size());

            for (AuthenticationListEntry e : this)
            {
                e.setAndHashPassword(e.getPassword());

                authList.put(e.getUsername(), e);
            }
        }

        return authList;
    }

    /**
     * Because AuthenticationLists are not ordered and cannot be randomly
     * accessed, add(int, AuthenticationListEntry) simply calls
     * add(AuthenticationListEntry).
     * 
     * @see ecologylab.services.authentication.AuthenticationList#add(ecologylab.services.authentication.AuthenticationListEntry)
     */
    @Override @Deprecated public void add(int i, AuthenticationListEntry obj)
            throws UnsupportedOperationException
    {
        this.add(obj);
    }
    
    /**
     * Because AuthenticationLists are not ordered and cannot be randomly
     * accessed, addAll(int, Collection) simply calls addAll(Collection).
     * 
     * @see ecologylab.services.authentication.AuthenticationList#add(java.util.Collection)
     */
    @Override @Deprecated public boolean addAll(int index, Collection c)
            throws UnsupportedOperationException
    {
        return addAll(c);
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#clear()
     */
    @Override @Deprecated public final void clear()
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "Cannot clear an AuthenticationList.");
    }

    /**
     * This operation is not supported for security purposes.
     * 
     * @see ecologylab.xml.subelements.ArrayListState#get(int)
     */
    @Override @Deprecated public final AuthenticationListEntry get(int i)
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "Cannot randomly access the contents of an AuthenticationList.");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#indexOf(java.lang.Object)
     */
    @Override @Deprecated public final int indexOf(Object elem)
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "Cannot randomly access the contents of an AuthenticationList");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#iterator()
     */
    @Override @Deprecated public final Iterator<AuthenticationListEntry> iterator()
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "Iterators are not allowed for AuthenticationLists");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#lastIndexOf(java.lang.Object)
     */
    @Override @Deprecated public final int lastIndexOf(Object elem)
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "lastIndexOf not supported for AuthenticationList.");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#listIterator()
     */
    @Override @Deprecated public final ListIterator<AuthenticationListEntry> listIterator()
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "Iterators are not allowed for AuthenticationLists");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#listIterator(int)
     */
    @Override @Deprecated public final ListIterator<AuthenticationListEntry> listIterator(
            int index) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "Iterators are not allowed for AuthenticationLists");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#remove(int)
     */
    @Override @Deprecated public final AuthenticationListEntry remove(int i)
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "Cannot remove elements from an AuthenticationList without username and password.");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#removeAll(java.util.Collection)
     */
    @Override @Deprecated public final boolean removeAll(Collection c)
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "removeAll is not supported by AuthenticationList");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#retainAll(java.util.Collection)
     */
    @Override @Deprecated public final boolean retainAll(Collection c)
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "retainAll is not supported by AuthenticationList");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#set(int,
     *      ecologylab.xml.ElementState)
     */
    @Override @Deprecated public final AuthenticationListEntry set(int index,
            AuthenticationListEntry element)
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "set(int, AuthenticationListEntry) is not allowed for AuthenticationList.");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#subList(int, int)
     */
    @Override @Deprecated public final List<AuthenticationListEntry> subList(
            int fromIndex, int toIndex) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "subList is not supported by AuthenticationListEntry.");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#toArray()
     */
    @Override @Deprecated public final Object[] toArray()
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "toArray is not supported by AuthenticationListEntry");
    }

    /**
     * @see ecologylab.xml.subelements.ArrayListState#toArray(T[])
     */
    @SuppressWarnings("unchecked") @Override @Deprecated public final AuthenticationListEntry[] toArray(
            Object[] a) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(
                "toArray is not supported by AuthenticationListEntry");
    }
}
