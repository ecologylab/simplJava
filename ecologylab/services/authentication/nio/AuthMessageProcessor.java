package ecologylab.services.authentication.nio;

import java.nio.channels.SelectionKey;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.MessageProcessor;
import ecologylab.services.nio.NIOServerBackend;
import ecologylab.xml.TranslationSpace;

public class AuthMessageProcessor extends MessageProcessor implements
        AuthServerRegistryObjects
{

    public AuthMessageProcessor(Object token, SelectionKey key,
            TranslationSpace translationSpace, ObjectRegistry registry,
            NIOServerBackend server)
    {
        super(token, key, translationSpace, registry, server);
    }

    protected ContextManager generateClientContext(Object token,
            SelectionKey key, TranslationSpace translationSpace,
            ObjectRegistry registry)
    {
        try
        {
            return new AuthContextManager(token, key, translationSpace,
                    registry, (AuthLogging) this.server);
        }
        catch (ClassCastException e)
        {
            debug("ATTEMPT TO USE AuthMessageProcessor WITH A NON-AUTHENTICATING SERVER!");
            e.printStackTrace();
        }
        
        return null;
    }

}
