package ecologylab.services.authentication.nio;

import java.nio.channels.SelectionKey;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.NIOServerBase;
import ecologylab.services.nio.two_threaded.MessageProcessor2Threads;
import ecologylab.xml.TranslationSpace;

public class AuthMessageProcessor2Threads extends MessageProcessor2Threads
        implements AuthServerRegistryObjects
{

    public AuthMessageProcessor2Threads(TranslationSpace translationSpace,
            ObjectRegistry registry, NIOServerBase server)
    {
        super(translationSpace, registry, server);
    }

    protected ContextManager generateClientContext(Object token,
            SelectionKey key, TranslationSpace translationSpace,
            ObjectRegistry registry)
    {
        try
        {
            return new AuthContextManager(token, key, translationSpace,
                    registry, (AuthLogging) server);
        }
        catch (ClassCastException e)
        {
            debug("ATTEMPT TO USE AuthMessageProcessor2Threads WITH A NON-AUTHENTICATING SERVER!");
            e.printStackTrace();
        }

        return null;
    }
}
