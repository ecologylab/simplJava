package ecologylab.services.authentication.nio;

import java.nio.channels.SelectionKey;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.MessageProcessor2Threads;
import ecologylab.xml.NameSpace;

public class AuthMessageProcessor2Threads extends MessageProcessor2Threads implements AuthServerRegistryObjects
{

    public AuthMessageProcessor2Threads(NameSpace translationSpace,
            ObjectRegistry registry)
    {
        super(translationSpace, registry);
    }

    protected ContextManager generateClientContext(Object token, SelectionKey key, NameSpace translationSpace, ObjectRegistry registry)
    {
        return new AuthContextManager(token, key, translationSpace, registry, (AuthLogging) registry.lookupObject(AUTH_SERVER));
    }
}
