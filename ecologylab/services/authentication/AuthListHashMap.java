/**
 * 
 */
package ecologylab.services.authentication;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.HashMapState;

/**
 * @author toupsz
 * 
 */
@xml_inherit public class AuthListHashMap extends HashMapState<String, AuthenticationListEntry>
{

    /**
     * 
     */
    public AuthListHashMap()
    {
    }

    @Override
    protected void createChildHook(ElementState child)
    {
        AuthenticationListEntry e = (AuthenticationListEntry) child;

        e.setAndHashPassword(e.getPassword());

        super.createChildHook(child);
    }

    public AuthenticationListEntry put(String key, AuthenticationListEntry value)
    {
        return super.put(key, value);
    }

}
