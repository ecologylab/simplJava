/**
 * 
 */
package ecologylab.oodss.distributed.impl;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;

import ecologylab.collections.Scope;
import ecologylab.net.NetTools;
import ecologylab.oodss.distributed.common.SessionObjects;
import ecologylab.oodss.distributed.server.NIOServerDataReader;
import ecologylab.oodss.distributed.server.clientsessionmanager.BaseSessionManager;
import ecologylab.oodss.messages.InitConnectionRequest;
import ecologylab.serialization.SimplTypesScope;

/**
 * Provides access to an NIOServerIOThread, which handles the details of network connections.
 * Subclasses extend and provide functionality for actually processing messages.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public abstract class AbstractNIOServer<S extends Scope> extends Manager implements
		NIOServerDataReader, Runnable, SessionObjects
{
	private NIOServerIOThread		backend;

	protected SimplTypesScope	translationScope;

	protected S									applicationObjectScope;

	/**
	 * Creates an instance of an NIOServer of some flavor. Creates the backend using the information
	 * in the arguments.
	 * 
	 * Registers itself as the MAIN_START_AND_STOPPABLE in the object registry.
	 * 
	 * @param portNumber
	 * @param inetAddress
	 * @param translationScope
	 * @param objectRegistry
	 * @throws IOException
	 * @throws BindException
	 */
	protected AbstractNIOServer(int portNumber, InetAddress[] inetAddress,
			SimplTypesScope requestTranslationSpace, S objectRegistry, int idleConnectionTimeout,
			int maxMessageLength) throws IOException, BindException
	{
		backend = this.generateBackend(portNumber, inetAddress, composeTranslations(portNumber,
				inetAddress[0], requestTranslationSpace), objectRegistry, idleConnectionTimeout,
				maxMessageLength);

		debug("setting up NIO Server...");

		// we get these from the backend, because it ensures that they are
		// configured if they are passed in null
		this.translationScope = backend.translationScope;
		this.applicationObjectScope = (S) backend.objectRegistry;

		this.applicationObjectScope.put(MAIN_START_AND_STOPPABLE, this);
		this.applicationObjectScope.put(MAIN_SHUTDOWNABLE, this);
	}

	static final Class[]	OUR_TRANSLATIONS	=
																					{ InitConnectionRequest.class, };

	public static SimplTypesScope composeTranslations(int portNumber, InetAddress inetAddress,
			SimplTypesScope requestTranslationSpace)
	{
		return composeTranslations(OUR_TRANSLATIONS, "nio_server_base: ", portNumber, inetAddress
				.toString(), requestTranslationSpace);
	}

	public static SimplTypesScope composeTranslations(Class[] newTranslations, String prefix,
			int portNumber, String inetAddress, SimplTypesScope requestTranslationSpace)
	{
		return SimplTypesScope.get(prefix + inetAddress.toString() + ":" + portNumber,
				requestTranslationSpace, newTranslations);
	}

	/**
	 * Creates an instance of an NIOServer of some flavor. Creates the backend using the information
	 * in the arguments.
	 * 
	 * Registers itself as the MAIN_START_AND_STOPPABLE in the object registry.
	 * 
	 * @param portNumber
	 * @param inetAddress
	 * @param translationScope
	 * @param objectRegistry
	 * @throws IOException
	 * @throws BindException
	 */
	protected AbstractNIOServer(int portNumber, InetAddress inetAddress,
			SimplTypesScope requestTranslationSpace, S objectRegistry, int idleConnectionTimeout,
			int maxMessageLength) throws IOException, BindException
	{
		this(portNumber, NetTools.wrapSingleAddress(inetAddress), requestTranslationSpace,
				objectRegistry, idleConnectionTimeout, maxMessageLength);
	}

	protected NIOServerIOThread generateBackend(int portNumber, InetAddress[] inetAddresses,
			SimplTypesScope requestTranslationSpace, S objectRegistry, int idleConnectionTimeout,
			int maxMessageLength) throws BindException, IOException
	{
		return NIOServerIOThread.getInstance(portNumber, inetAddresses, this, requestTranslationSpace,
				objectRegistry, idleConnectionTimeout, maxMessageLength);
	}

	protected abstract BaseSessionManager generateContextManager(String sessionId, SelectionKey sk,
			SimplTypesScope translationScope, Scope globalScope);

	/**
	 * @see ecologylab.generic.StartAndStoppable#start()
	 */
	@Override
	public void start()
	{
		try
		{
			backend.openSelector();
			backend.registerAcceptWithSelector();
			backend.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @see ecologylab.generic.StartAndStoppable#stop()
	 */
	@Override
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
		return applicationObjectScope;
	}

	/**
	 * @return the translationScope
	 */
	public SimplTypesScope getTranslationSpace()
	{
		return translationScope;
	}
}
