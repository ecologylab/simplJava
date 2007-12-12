/**
 * 
 */
package ecologylab.services.distributed.common;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

import ecologylab.services.exceptions.BadClientException;
import ecologylab.services.exceptions.ClientOfflineException;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public interface NetworkingConstants
{
	/** the maximum size of message acceptable by server in encoded CHARs */
	static final int					MAX_PACKET_SIZE_CHARACTERS		= 128 * 1024;

	/** The maximum size an http-like header on a message may be, in bytes. */
	static final int					MAX_HTTP_HEADER_LENGTH			= 4096;

	/** The content-length http-like header indicator. */
	static final String				CONTENT_LENGTH_STRING			= "content-length";

	static final String				UNIQUE_IDENTIFIER_STRING		= "uid";

	static final String				HTTP_HEADER_LINE_DELIMITER		= "\r\n";

	/** The terminator string for the end of http-like headers. */
	static final String				HTTP_HEADER_TERMINATOR			= HTTP_HEADER_LINE_DELIMITER
																							+ HTTP_HEADER_LINE_DELIMITER;

	/** The size of the content-length header indicator. */
	static final int					CONTENT_LENGTH_STRING_LENGTH	= CONTENT_LENGTH_STRING.length();

	/** Character encoding for messages sent through the network. */
	static final String				CHARACTER_ENCODING				= "US-ASCII";

	/** The encoder to translate from Strings to bytes. */
	static final CharsetEncoder	ENCODER								= Charset.forName(CHARACTER_ENCODING).newEncoder();

	/** The decoder to translate from bytes to Strings. */
	static final CharsetDecoder	DECODER								= Charset.forName(CHARACTER_ENCODING).newDecoder();

	/** the maximum size of message acceptable by server in encoded BYTEs */
	static final int					MAX_PACKET_SIZE_BYTES			= (int) Math.ceil(MAX_PACKET_SIZE_CHARACTERS
																							* ENCODER.maxBytesPerChar());
}
