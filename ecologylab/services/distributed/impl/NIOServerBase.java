/**
 * 
 */
package ecologylab.services.distributed.impl;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.generic.Debug;
import ecologylab.generic.StartAndStoppable;
import ecologylab.services.SessionObjects;
import ecologylab.services.Shutdownable;
import ecologylab.services.distributed.server.NIOServerFrontend;
import ecologylab.services.distributed.server.contextmanager.AbstractContextManager;
import ecologylab.services.messages.InitConnectionRequest;
import ecologylab.xml.TranslationSpace;

/**
 * Combines an NIOServerBackend and NIOServerFrontend
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public abstract class NIOServerBase extends Debug implements NIOServerFrontend, Runnable, StartAndStoppable,
		SessionObjects, Shutdownable
{
	private NIOServerBackend	backend;

	protected TranslationSpace	translationSpace;

	protected ObjectRegistry	registry;

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
	 * Creates an instance of an NIOServer of some flavor. Creates the backend using the information in the arguments.
	 * 
	 * Registers itself as the MAIN_START_AND_STOPPABLE in the object registry.
	 * 
	 * @param portNumber
	 * @param inetAddress
	 * @param requestTranslationSpace
	 * @param objectRegistry
	 * @throws IOException
	 * @throws BindException
	 */
	protected NIOServerBase(int portNumber, InetAddress[] inetAddress, TranslationSpace requestTranslationSpace,
			ObjectRegistry objectRegistry, int idleConnectionTimeout) throws IOException, BindException
	{
		backend = this.generateBackend(portNumber, inetAddress,
				// requestTranslationSpace,
				composeTranslations(portNumber, inetAddress[0], requestTranslationSpace), objectRegistry,
				idleConnectionTimeout);

		this.translationSpace = requestTranslationSpace;
		this.registry = objectRegistry;

		registry.registerObject(MAIN_START_AND_STOPPABLE, this);
		registry.registerObject(MAIN_SHUTDOWNABLE, this);

		// this.translationSpace.addTranslation(InitConnectionRequest.class);
	}

	static final Class[]	OUR_TRANSLATIONS	=
														{ InitConnectionRequest.class, };

	public static TranslationSpace composeTranslations(int portNumber, InetAddress inetAddress,
			TranslationSpace requestTranslationSpace)
	{
		return composeTranslations(OUR_TRANSLATIONS, "nio_server_base: ", portNumber, inetAddress.toString(),
				requestTranslationSpace);
	}

	public static TranslationSpace composeTranslations(Class[] newTranslations, String prefix, int portNumber,
			String inetAddress, TranslationSpace requestTranslationSpace)
	{
		return TranslationSpace.get(prefix + inetAddress.toString() + ":" + portNumber, newTranslations,
				requestTranslationSpace);
	}

	/**
	 * Creates an instance of an NIOServer of some flavor. Creates the backend using the information in the arguments.
	 * 
	 * Registers itself as the MAIN_START_AND_STOPPABLE in the object registry.
	 * 
	 * @param portNumber
	 * @param inetAddress
	 * @param requestTranslationSpace
	 * @param objectRegistry
	 * @throws IOException
	 * @throws BindException
	 */
	protected NIOServerBase(int portNumber, InetAddress inetAddress, TranslationSpace requestTranslationSpace,
			ObjectRegistry objectRegistry, int idleConnectionTimeout) throws IOException, BindException
	{
		backend = this.generateBackend(portNumber, inetAddress,
				// requestTranslationSpace,
				composeTranslations(portNumber, inetAddress, requestTranslationSpace), objectRegistry,
				idleConnectionTimeout);

		this.translationSpace = requestTranslationSpace;
		this.registry = objectRegistry;

		registry.registerObject(MAIN_START_AND_STOPPABLE, this);
		registry.registerObject(MAIN_SHUTDOWNABLE, this);

		// this.translationSpace.addTranslation(InitConnectionRequest.class);
	}

	protected NIOServerBackend generateBackend(int portNumber, InetAddress[] inetAddresses,
			TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry, int idleConnectionTimeout)
			throws BindException, IOException
	{
		return NIOServerBackend.getInstance(portNumber, inetAddresses, this, requestTranslationSpace, objectRegistry,
				idleConnectionTimeout);
	}

	protected NIOServerBackend generateBackend(int portNumber, InetAddress inetAddress,
			TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry, int idleConnectionTimeout)
			throws BindException, IOException
	{
		return NIOServerBackend.getInstance(portNumber, inetAddress, this, requestTranslationSpace, objectRegistry,
				idleConnectionTimeout);
	}

	protected abstract AbstractContextManager generateContextManager(Object token, SocketChannel sc,
			TranslationSpace translationSpace, ObjectRegistry registry);

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
