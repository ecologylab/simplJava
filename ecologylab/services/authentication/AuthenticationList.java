/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.util.HashMap;
import java.util.Iterator;

import ecologylab.xml.ArrayListState;
import ecologylab.xml.XmlTranslationException;

/**
 * Contains a HashMap of AuthenticationListEntry's that are hashed on their
 * username values.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class AuthenticationList extends ArrayListState
{
    private HashMap authList = null;

    public AuthenticationList()
    {
        super();
    }

    /**
     * Adds an entry to the authentication list.
     * 
     * @param entry -
     *            the list entry to add to the list.
     */
    public void add(AuthenticationListEntry entry)
    {
        this.add(entry);

        authList().put(entry.getUsername(), entry);
    }

    /**
     * Constructs the internal HashMap to improve retrievals. This should
     * probably be made more efficient later.
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
    private HashMap authList()
    {
        if (authList == null)
        {
            authList = new HashMap();

            Iterator previousEntries = this.iterator();

            while (previousEntries.hasNext())
            {
                AuthenticationListEntry entry = (AuthenticationListEntry) previousEntries
                        .next();

                entry.setAndHashPassword(entry.getPassword());

                authList.put(entry.getUsername(), entry);
            }
        }
        return authList;
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
        return (AuthenticationListEntry) authList().get(username.toLowerCase());
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
        return authList().containsKey(username.toLowerCase());
    }

    public String toString()
    {
        Iterator tempIter = authList().values().iterator();
        StringBuffer buffy = new StringBuffer();

        while (tempIter.hasNext())
        {
            String entryXML;
            try
            {
                entryXML = ((AuthenticationListEntry) tempIter.next())
                        .translateToXML(false);
                buffy.append(entryXML).append('\n');
            }
            catch (XmlTranslationException e)
            {
                e.printStackTrace();
                System.out.println("exception caught; skipping bad entry");
            }
        }

        return buffy.toString();
    }
}
