package ecologylab.services.authentication.nio;

import java.nio.channels.SelectionKey;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.MessageProcessor;
import ecologylab.xml.TranslationSpace;

public class AuthMessageProcessor extends MessageProcessor implements AuthServerRegistryObjects
{

    public AuthMessageProcessor(Object token, SelectionKey key, TranslationSpace translationSpace,
            ObjectRegistry registry)
    {
        super(token, key, translationSpace, registry);
    }

    protected ContextManager generateClientContext(Object token, SelectionKey key, TranslationSpace translationSpace, ObjectRegistry registry)
    {
        return new AuthContextManager(token, key, translationSpace, registry, (AuthLogging) registry.lookupObject(AUTH_SERVER));
    }
    
}
