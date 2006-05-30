/*
 * Created on May 15, 2006
 */
package ecologylab.services.authentication.nio;

import java.io.IOException;
import java.net.BindException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.authentication.AuthenticationList;
import ecologylab.services.authentication.messages.Login;
import ecologylab.services.authentication.messages.Logout;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.nio.NIOServer1Thread;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpace;
import ecologylab.xml.XmlTranslationException;

public class NIOAuthServerSingleThreaded extends NIOServer1Thread implements
AuthServerRegistryObjects
{

    /**
     * This is the actual way to create an instance of this.
     * 
     * @param portNumber
     * @param requestTranslationSpace
     * @param objectRegistry
     * @param authListFilename -
     *            a file name indicating the location of the authentication
     *            list; this should be an XML file of an AuthenticationList
     *            object.
     * @return A server instance, or null if it was not possible to open a
     *         ServerSocket on the port on this machine.
     */
    public static NIOAuthServerSingleThreaded get(int portNumber,
            NameSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            String authListFilename)
    {
        NIOAuthServerSingleThreaded newServer = null;
        
        try
        {
            AuthenticationList authList = (AuthenticationList) ElementState
                    .translateFromXML(authListFilename, NameSpace.get(
                            "authListNameSpace",
                            "ecologylab.services.authentication"));
            newServer = new NIOAuthServerSingleThreaded(portNumber,
                    requestTranslationSpace, objectRegistry, authList);
        } catch (IOException e)
        {
            println("ServicesServer ERROR: can't open ServerSocket on port "
                    + portNumber);
            e.printStackTrace();
        } catch (XmlTranslationException e)
        {
            e.printStackTrace();
        }
        
        return newServer;
    }

    /**
     * This is the actual way to create an instance of this.
     * 
     * @param portNumber
     * @param requestTranslationSpace
     * @param objectRegistry
     * @param authList -
     *            the AuthorizationList object to be used to determine possible
     *            users.
     * @return A server instance, or null if it was not possible to open a
     *         ServerSocket on the port on this machine.
     */
    public static NIOAuthServerSingleThreaded get(int portNumber,
            NameSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            AuthenticationList authList)
    {
        NIOAuthServerSingleThreaded newServer = null;
        
        try
        {
            newServer = new NIOAuthServerSingleThreaded(portNumber,
                    requestTranslationSpace, objectRegistry, authList);
        } catch (IOException e)
        {
            println("ServicesServer ERROR: can't open ServerSocket on port "
                    + portNumber);
            e.printStackTrace();
        }

        return newServer;
    }

    /**
     * Creates a new AuthServer with the given arguments. This
     * constructor should only be invoked by a subclass or the .get() method to
     * ensure that only one server is running on the given port.
     * 
     * @param portNumber
     * @param requestTranslationSpace
     * @param objectRegistry
     * @param authList
     * @throws IOException
     * @throws BindException
     */
    protected NIOAuthServerSingleThreaded(int portNumber,
            NameSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            AuthenticationList authList) throws IOException, BindException
    {
        super(portNumber, requestTranslationSpace, objectRegistry);

        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages", "Login");
        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages", "Logout");
        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages", "LoginStatusResponse");
        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages", "LogoutStatusResponse");
        
        this.objectRegistry.registerObject(AUTHENTICATION_LIST, authList);

        this.objectRegistry
                .registerObject(AUTHENTICATED_CLIENTS_BY_USERNAME, new HashMap());
        this.objectRegistry.registerObject(AUTHENTICATED_CLIENTS_BY_TOKEN, new HashMap());
    }
}
