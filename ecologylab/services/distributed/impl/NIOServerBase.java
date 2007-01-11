/**
 * 
 */
package ecologylab.services.nio.servers;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.generic.Debug;
import ecologylab.generic.StartAndStoppable;
import ecologylab.services.SessionObjects;
import ecologylab.services.Shutdownable;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.NIOServerBackend;
import ecologylab.xml.TranslationSpace;

/**
 * @author Zach Toups
 * 
 */
public abstract class NIOServerBase extends Debug implements NIOServerFrontend,
        Runnable, StartAndStoppable, SessionObjects, Shutdownable
{
    private NIOServerBackend backend;

    protected TranslationSpace translationSpace;

    protected ObjectRegistry   registry;

    /**
     * @return the backend
     */
    public NIOServerBackend getBackend()
    {
        return backend;
    }

    /**
     * @return the registry
     */
    public ObjectRegistry getRegistry()
    {
        return registry;
    }

    /**
     * @return the translationSpace
     */
    public TranslationSpace getTranslationSpace()
    {
        return translationSpace;
    }

    /**
     * Creates an instance of an NIOServer of some flavor. Creates the backend
     * using the information in the arguments.
     * 
     * Registers itself as the MAIN_START_AND_STOPPABLE in the object registry.
     * 
     * @param portNumber
     * @param inetAddress
     * @param sAP
     * @param requestTranslationSpace
     * @param objectRegistry
     * @throws IOException
     * @throws BindException
     */
    protected NIOServerBase(int portNumber, InetAddress inetAddress,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout) throws IOException, BindException
    {
        backend = NIOServerBackend.getInstance(portNumber, inetAddress, this,
                requestTranslationSpace, objectRegistry, idleConnectionTimeout);

        this.translationSpace = requestTranslationSpace;
        this.registry = objectRegistry;
        
        registry.registerObject(MAIN_START_AND_STOPPABLE, this);
        registry.registerObject(MAIN_SHUTDOWNABLE, this);
    }
    
    protected abstract ContextManager generateContextManager(Object token,
            SocketChannel sc, TranslationSpace translationSpace,
            ObjectRegistry registry);

    /**
     * @see ecologylab.generic.StartAndStoppable#start()
     */
    public void start()
    {
        backend.start();
    }

    /**
     * @see ecologylab.generic.StartAndStoppable#stop()
     */
    public void stop()
    {
        backend.stop();
    }
}
