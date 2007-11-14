/*
 * Created on May 3, 2006
 */
package ecologylab.services.distributed.common;

/**
 * Constants used by ServicesServers and their components.
 */
public interface ServerConstants
{
	/** If we get more bad messages than this, it may be malicous. */
	static final int		MAXIMUM_TRANSMISSION_ERRORS			= 3;

	/** the maximum size of message acceptable by server */
	static final int		MAX_PACKET_SIZE							= 128 * 1024;

	/** Limit the maximum number of client connection to the server */
	static final int		MAX_CONNECTIONS							= 100;

	/** The maximum size an http-like header on a message may be, in bytes. */
	static final int		MAX_HTTP_HEADER_LENGTH					= 4096;

	/** Character encoding for messages sent through the network. */
	static final String	CHARACTER_ENCODING						= "US-ASCII";

	/** Maximum amount of time, in milliseconds, a connection can be idle, even if we don't want to drop idle connections. */
	static final int		GARBAGE_CONNECTION_CLEANUP_TIMEOUT	= 8 * 60 * 60 * 1000;					// 8 hours

	/** The content-length http-like header indicator. */
	static final String	CONTENT_LENGTH_STRING					= "content-length";

	/** The terminator string for the end of http-like headers. */
	static final String	HTTP_HEADER_TERMINATOR					= "\r\n\r\n";

	/** The size of the content-length header indicator. */
	static final int		CONTENT_LENGTH_STRING_LENGTH			= CONTENT_LENGTH_STRING.length();
	
	/** The size of the HTTP header terminator. */
	static final int		HTTP_HEADER_TERMINATOR_LENGTH			= HTTP_HEADER_TERMINATOR.length();
}
