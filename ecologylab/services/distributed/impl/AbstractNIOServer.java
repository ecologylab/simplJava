/**
 * 
 */
package ecologylab.services.distributed.impl;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;

import ecologylab.collections.Scope;
import ecologylab.generic.StartAndStoppable;
import ecologylab.net.NetTools;
import ecologylab.services.distributed.common.SessionObjects;
import ecologylab.services.distributed.server.NIOServerProcessor;
import ecologylab.services.distributed.server.clientmanager.AbstractClientManager;
import ecologylab.services.messages.InitConnectionRequest;
import ecologylab.xml.TranslationSpace;

/**
 * Provides access to an NIOServerIOThread, which handles the details of network
 * connections. Subclasses extend and provide functionality for actually
 * processing messages.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public abstract class AbstractNIOServer extends Manager implements
		NIOServerProcessor, Runnable, StartAndStoppable, SessionObjects
{
	private NIOServerIOThread		backend;

	protected TranslationSpace		translationSpace;

	protected Scope					globalScope;

	/**
	 * Creates an instance of an NIOServer of some flavor. Creates the backend
	 * using the information in the arguments.
	 * 
	 * Registers itself as the MAIN_START_AND_STOPPABLE in the object registry.
	 * 
	 * @param portNumber
	 * @param inetAddress
	 * @param translationSpace
	 * @param objectRegistry
	 * @throws IOException
	 * @throws BindException
	 */
	protected AbstractNIOServer(int portNumber, InetAddress[] inetAddress,
			TranslationSpace requestTranslationSpace, Scope objectRegistry,
			int idleConnectionTimeout) throws IOException, BindException
	{
		backend = this.generateBackend(portNumber, inetAddress,
				composeTranslations(portNumber, inetAddress[0],
						requestTranslationSpace), objectRegistry,
				idleConnectionTimeout);

		debug("setting up NIO Server...");

		// we get these from the backend, because it ensures that they are
		// configured if they are passed in null
		this.translationSpace = backend.translationSpace;
		this.globalScope = backend.objectRegistry;

		this.globalScope.put(MAIN_START_AND_STOPPABLE, this);
		this.globalScope.put(MAIN_SHUTDOWNABLE, this);
	}

	static final Class[]	OUR_TRANSLATIONS	=
														{ InitConnectionRequest.class, };

	public static TranslationSpace composeTranslations(int portNumber,
			InetAddress inetAddress, TranslationSpace requestTranslationSpace)
	{
		return composeTranslations(OUR_TRANSLATIONS, "nio_server_base: ",
				portNumber, inetAddress.toString(), requestTranslationSpace);
	}

	public static TranslationSpace composeTranslations(Class[] newTranslations,
			String prefix, int portNumber, String inetAddress,
			TranslationSpace requestTranslationSpace)
	{
		return TranslationSpace.get(prefix + inetAddress.toString() + ":"
				+ portNumber, newTranslations, requestTranslationSpace);
	}

	/**
	 * Creates an instance of an NIOServer of some flavor. Creates the backend
	 * using the information in the arguments.
	 * 
	 * Registers itself as the MAIN_START_AND_STOPPABLE in the object registry.
	 * 
	 * @param portNumber
	 * @param inetAddress
	 * @param translationSpace
	 * @param objectRegistry
	 * @throws IOException
	 * @throws BindException
	 */
	protected AbstractNIOServer(int portNumber, InetAddress inetAddress,
			TranslationSpace requestTranslationSpace, Scope objectRegistry,
			int idleConnectionTimeout) throws IOException, BindException
	{
		this(portNumber, NetTools.wrapSingleAddress(inetAddress),
				requestTranslationSpace, objectRegistry, idleConnectionTimeout);
	}

	protected NIOServerIOThread generateBackend(int portNumber,
			InetAddress[] inetAddresses, TranslationSpace requestTranslationSpace,
			Scope objectRegistry, int idleConnectionTimeout) throws BindException,
			IOException
	{
		return NIOServerIOThread.getInstance(portNumber, inetAddresses, this,
				requestTranslationSpace, objectRegistry, idleConnectionTimeout);
	}

	protected abstract AbstractClientManager generateContextManager(
			Object token, SelectionKey sk, TranslationSpace translationSpace,
			Scope globalScope);

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
	
	/**
	 * @return the backend
	 */
	public NIOServerIOThread getBackend()
	{
		return backend;
	}

	/**
	 * @return the global scope for this server
	 */
	public Scope getGlobalScope()
	{
		return globalScope;
	}

	/**
	 * @return the translationSpace
	 */
	public TranslationSpace getTranslationSpace()
	{
		return translationSpace;
	}
}
