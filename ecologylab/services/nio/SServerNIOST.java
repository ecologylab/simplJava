/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio;

import java.io.IOException;
import java.net.BindException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpace;
import ecologylab.xml.XmlTranslationException;

public class SServerNIOST extends NIOServicesServerBase
        implements ServerConstants
{
    protected ObjectRegistry              registry;

    /**
     * Maps key attachments (different connections) to accumulators
     * (StringBuffers of the incoming messages recieved so far).
     */
    private HashMap<Object, StringBuffer> connectionAccumulators = new HashMap<Object, StringBuffer>();

    // private Charset charset = Charset.forName("ISO-8859-1");
    private Charset                       charset                = Charset
                                                                         .forName("ASCII");

    private CharsetDecoder                decoder                = charset
                                                                         .newDecoder();

    private CharsetEncoder                encoder                = charset
                                                                         .newEncoder();

    public SServerNIOST(int portNumber,
            NameSpace requestTranslationSpace, ObjectRegistry objectRegistry)
            throws IOException, BindException
    {
        super(portNumber, requestTranslationSpace, objectRegistry);
    }

    /**
     * Use the ServicesServer and its ObjectRegistry to do the translation. Can
     * be overridden to provide special functionalities
     * 
     * @param messageString
     * @return
     * @throws XmlTranslationException
     */
    protected RequestMessage translateXMLStringToRequestMessage(
            String messageString) throws XmlTranslationException
    {
        return translateXMLStringToRequestMessage(messageString, true);
    }

    public RequestMessage translateXMLStringToRequestMessage(
            String messageString, boolean doRecursiveDescent)
            throws XmlTranslationException
    {
        return (RequestMessage) ElementState.translateFromXMLString(
                messageString, requestTranslationSpace, doRecursiveDescent);
    }

    private void processString(String incomingMessage, SelectionKey key)
    {
        RequestMessage request = null;
        ResponseMessage response = null;
        CharBuffer outgoingChars = CharBuffer.allocate(MAX_PACKET_SIZE);

        try
        {
            request = translateXMLStringToRequestMessage(incomingMessage);

        }
        catch (XmlTranslationException e)
        {
            e.printStackTrace();
        }

        if (request == null)
        {
            debug("ERROR: translation failed: ");

        }
        else
        {
            // perform the service being requested
            response = performService(request);

            if (response != null)
            { // if the response is null, then we do nothing else
                try
                {
//                     System.out.println("response: "
  //                   + response.translateToXML(false)+"\n");
                    // translate the response and store it, then
                    // encode it and write it
                    outgoingChars.clear();
                    outgoingChars.put(response.translateToXML(false)).put('\n');
                    outgoingChars.flip();

                    ((SocketChannel) key.channel()).write(encoder
                            .encode(outgoingChars));

                }
                catch (XmlTranslationException e)
                {
                    e.printStackTrace();
                }
                catch (CharacterCodingException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            } else
            {
                debug("Response turned out null.");
            }

        }
    }

    protected void invalidateKey(SelectionKey key)
    {
        debug("Key " + key.attachment()
                + " invalid; shutting down message processor.");

        if (connectionAccumulators.containsKey(key.attachment()))
        {
            connectionAccumulators.remove(key.attachment());
        }

        key.cancel();
    }

    protected void readKey(SelectionKey key)
    { // incoming message

        ByteBuffer rawBytes = ByteBuffer.allocate(MAX_PACKET_SIZE);
        int bytesRead = 0;

        StringBuffer accumulator = connectionAccumulators.get(key.attachment());

        if (accumulator == null)
        { // no accumulator for this key yet
            connectionAccumulators.put(key.attachment(), new StringBuffer(
                    MAX_PACKET_SIZE));
            accumulator = connectionAccumulators.get(key.attachment());
        }

        try
        {
            while ((bytesRead = ((SocketChannel) key.channel()).read(rawBytes)) > 0)
            {
                if (bytesRead < MAX_PACKET_SIZE)
                {
                    rawBytes.flip();

                    accumulator.append(decoder.decode(rawBytes));

                    rawBytes.clear();

                    if (accumulator.length() > 0)
                    {
                        if ((accumulator.charAt(accumulator.length() - 1) == '\n')
                                || (accumulator
                                        .charAt(accumulator.length() - 1) == '\r'))
                        { // when we have accumulated
                            // an entire message,
                            // process it

                            // in case we have several
                            // messages that are
                            // split by returns
                            while (accumulator.length() > 0)
                            {
                                // transform the message
                                // into a request and
                                // perform the service
                                // long time =
                                // System.currentTimeMillis();

                                processString(accumulator.substring(0,
                                        accumulator.indexOf("\n")), key);

                                // System.out.println("time:
                                // "+(System.currentTimeMillis()-time));

                                // erase the message
                                // from the accumulator
                                accumulator.delete(0,
                                        accumulator.indexOf("\n") + 1);
                            }
                        }
                    }
                }
                else
                { // TODO might be able to catch too
                    // large messages
                    // better.
                    debug("Packet too large. Terminating connection.");

                    key.cancel();
                }
            }
        }
        catch (CharacterCodingException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }
}
