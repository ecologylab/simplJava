/**
 * 
 */
package ecologylab.services.distributed.common;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public interface NetworkingConstants
{
	/** the maximum size of message acceptable by server in encoded CHARs */
	static final int						DEFAULT_MAX_MESSAGE_LENGTH_CHARS	= 128 * 1024;										// 128KB

	static final int						DEFAULT_IDLE_TIMEOUT							= 10000;

	/** The maximum size an http-like header on a message may be, in bytes. */
	static final int						MAX_HTTP_HEADER_LENGTH						= 4 * 1024;											// 4KB

	/** The content-length http-like header indicator. */
	static final String					CONTENT_LENGTH_STRING							= "content-length";

	static final String					UNIQUE_IDENTIFIER_STRING					= "uid";

	static final String					HTTP_HEADER_LINE_DELIMITER				= "\r\n";

	/** The terminator string for the end of http-like headers. */
	static final String					HTTP_HEADER_TERMINATOR						= HTTP_HEADER_LINE_DELIMITER
																																		+ HTTP_HEADER_LINE_DELIMITER;

	/** Content coding specifies whether or not to some type of comression is used in the message */
	static final String					HTTP_CONTENT_CODING							= "content-encoding";
	
	/** Specifies what decoding schemes are acceptable to send back to the the client */
	static final String					HTTP_ACCEPTED_ENCODINGS						= "accept-encoding:deflate";
	
	static final String					HTTP_ACCEPT_ENCODING						= "accept-encoding";
	
	/** String specifying deflate encoding */
	static final String					HTTP_DEFLATE_ENCODING					= "deflate";
	
	/** The size of the content-length header indicator. */
	static final int						CONTENT_LENGTH_STRING_LENGTH			= CONTENT_LENGTH_STRING.length();

	/** Character encoding for messages sent through the network. */
	static final String					CHARACTER_ENCODING								= "ISO-8859-1";

	/** Charset for CHARACTER_ENCODING; to avoid calling forName too much. */
	static final Charset				CHARSET														= Charset
																																		.forName(CHARACTER_ENCODING);
}
