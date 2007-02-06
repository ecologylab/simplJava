/**
 * 
 */
package ecologylab.services.nio;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.net.ParsedURL;
import ecologylab.services.messages.HttpGetRequest;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * @author Zach
 * 
 */
public class HTTPGetContextManager extends ContextManager
{
    static final String         HTTP_PREPEND              = "GET /";

    static final int            HTTP_PREPEND_LENGTH       = HTTP_PREPEND
                                                                  .length();


    // private static final String SCRIPT_HREF_EQUALS_BLANK = "<html>\r\n<script
    // language=\"JavaScript\">\r\nself.location.href =
    // \"about:blank\";\r\n</script>\r\n</html>\r\n";
    private static final String SCRIPT_HREF_EQUALS_BLANK  = "<html>\r\n<body bgcolor=red onload=\"alert('yo');location.href = 'http://atsia.csdl.tamu.edu/andruid/ecologylab/combinFormation/launch/green.html'; \">\r\n</body>\r\n</html>\r\n";

    // private static final String SCRIPT_HREF_EQUALS_BLANK = "<html>\r\n<script
    // language=\"JavaScript\">alert('yo!');\r\nself.location.href =
    // \"about:blank\";\r\n</script>\r\n</html>\r\n";
    private static final byte[] HREF_EQUALS_BLANK_BYTES   = SCRIPT_HREF_EQUALS_BLANK
                                                                  .getBytes();

    private static final int    HREF_EQUALS_BLANK_LENGTH  = HREF_EQUALS_BLANK_BYTES.length;

    private static final String CONTENT_LENGTH            = "Content-Length: "
                                                                  + HREF_EQUALS_BLANK_LENGTH
                                                                  + "\r\n\r\n";

    private static final String HTTP_VERSION              = "HTTP/1.1";

    private static final String HTTP_RESPONSE_HEADERS     = HTTP_VERSION
                                                                  + " 307 Temporary Redirect"
                                                                  + "\r\n"
                                                                  + "Date: Fri, 17 Nov 2006 05:21:59 GMT\r\n";

    static final String         HTTP_APPEND               = " " + HTTP_VERSION;

    static final int            HTTP_APPEND_LENGTH        = HTTP_APPEND
                                                                  .length();

    // + "Content-Type: text/html\r\n" + CONTENT_LENGTH;
    protected boolean           ALLOW_HTTP_STYLE_REQUESTS = true;

    /**
     * @param token
     * @param server
     * @param socket
     * @param translationSpace
     * @param registry
     */
    public HTTPGetContextManager(Object token, NIOServerBackend server,
            SocketChannel socket, TranslationSpace translationSpace,
            ObjectRegistry registry)
    {
        super(token, server, socket, translationSpace, registry);
    }

    /**
     * @see ecologylab.services.nio.ContextManager#translateResponseMessageToString(ecologylab.services.messages.RequestMessage,
     *      ecologylab.services.messages.ResponseMessage)
     */
    @Override protected String translateResponseMessageToString(
            RequestMessage requestMessage, ResponseMessage responseMessage)
            throws XmlTranslationException
    {
        if (requestMessage instanceof HttpGetRequest)
        {
            HttpGetRequest httpRequest = (HttpGetRequest) requestMessage;

            ParsedURL responseUrl = null;
            if (responseMessage instanceof OkResponse)
                responseUrl = httpRequest.okResponseUrl();
            else
                responseUrl = httpRequest.errorResponseUrl();

            String responseString;
            debugA("responseUrl: " + responseUrl);
            if (responseUrl != null)
                responseString = HTTP_RESPONSE_HEADERS + "Location: "
                        + responseUrl.toString() + "\r\n\r\n";
            else
            {
                // TODO handle case where no response url is provided
                responseString = "";
            }
            debugA("Server sending response!!!\n" + responseString);

            return responseString;
        }
        else
        {
            return super.translateResponseMessageToString(requestMessage,
                    responseMessage);
        }

    }

    /**
     * @throws UnsupportedEncodingException
     * @see ecologylab.services.nio.ContextManager#translateXMLStringToRequestMessage(java.lang.String)
     */
    @Override protected RequestMessage translateXMLStringToRequestMessage(
            String messageString) throws XmlTranslationException,
            UnsupportedEncodingException
    {
        if (messageString.startsWith(HTTP_PREPEND))
        {
            int endIndex = messageString.lastIndexOf(HTTP_APPEND);
            messageString = messageString.substring(HTTP_PREPEND_LENGTH,
                    endIndex);
            messageString = URLDecoder.decode(messageString, "UTF-8");
            debug("fixed message! " + messageString);
        }

        return super.translateXMLStringToRequestMessage(messageString);
    }

}
