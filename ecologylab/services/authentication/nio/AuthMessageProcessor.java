package ecologylab.services.authentication.nio;

import java.nio.channels.SelectionKey;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.MessageProcessor;
import ecologylab.xml.NameSpace;

public class AuthMessageProcessor extends MessageProcessor
{

    public AuthMessageProcessor(Object token, SelectionKey key, NameSpace translationSpace,
            ObjectRegistry registry)
    {
        super(token, key, translationSpace, registry);
    }

    protected ContextManager generateClientContext(Object token, SelectionKey key, NameSpace translationSpace, ObjectRegistry registry)
    {
        return new AuthContextManager(token, key, translationSpace, registry);
    }
    
}
