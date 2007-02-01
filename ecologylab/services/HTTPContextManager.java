/**
 * 
 */
package ecologylab.services;

import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.NIOServerBackend;
import ecologylab.xml.TranslationSpace;

/**
 * @author Zach
 *
 */
public class HTTPContextManager extends ContextManager
{

    /**
     * @param token
     * @param server
     * @param socket
     * @param translationSpace
     * @param registry
     */
    public HTTPContextManager(Object token, NIOServerBackend server,
            SocketChannel socket, TranslationSpace translationSpace,
            ObjectRegistry registry)
    {
        super(token, server, socket, translationSpace, registry);
        // TODO Auto-generated constructor stub
    }

}
