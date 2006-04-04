/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.util.HashMap;

import ecologylab.xml.ElementState;
import ecologylab.xml.XmlTranslationException;

/**
 * Contains a HashMap of AuthenticationListEntry's that are hashed on their username values.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class AuthenticationList extends ElementState {

    public HashMap authList = new HashMap();
    
    public AuthenticationList() {
        super();
    }

    /** @override
     * makes sure that the reconstructed AuthenticationList is hashed on the username of the entries.
     */
    public void addNestedElement(ElementState elementState)
    throws XmlTranslationException {
        authList.put(((AuthenticationListEntry)elementState).getUsername(), (AuthenticationListEntry)elementState);
    }
    
    public void add(AuthenticationListEntry entry) {
        authList.put(entry.getUsername(), entry);
    }
    
    public AuthenticationListEntry get(String username) {
        return (AuthenticationListEntry)authList.get(username);
    }

    public boolean containsKey(String username) {
        return authList.containsKey(username);
    }
    
}
