/*
 * Created on Mar 30, 2006
 */
package ecologylab.services;

import java.io.IOException;
import java.net.Socket;

import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.messages.ResponseTypes;
import ecologylab.xml.XmlTranslationException;

public class ServerToClientConnectionAuthentication extends
        ServerToClientConnection implements ResponseTypes {

    private boolean loggedIn = false;

    private RequestMessage requestMessage;

    private ResponseMessage responseMessage;

    public ServerToClientConnectionAuthentication(Socket incomingSocket,
            ServicesServer servicesServer) throws IOException {
        super(incomingSocket, servicesServer);
    }

    /**
     * Service the client connection.
     */
    public void run() {
        while (running) {
            // debug("waiting for packet");
            // get the packet message
            String messageString = "";
            try {
                // TODO -- change to nio
                if (inputStreamReader.ready()) {
                    messageString = inputStreamReader.readLine();

                //    debugA("got raw message: " + messageString);

                    if (messageString != null) {
                        requestMessage = servicesServer
                                .translateXMLStringToRequestMessage(messageString);
                        System.out.println(requestMessage.translateToXML(false));
                    } else {
                        requestMessage = null;
                    }

                    if (requestMessage == null) {
                        debug("ERROR: translation failed: " + messageString);
                    } else {
                        // if not logged in yet, make sure they log in first
                        if (!loggedIn) {
                            if (requestMessage instanceof ecologylab.services.messages.Login) {
                                // perform the service being requested, if
                                // they're logged in
                                responseMessage = servicesServer
                                        .performService(requestMessage);

                                if (responseMessage.getResponse().equals(LOGIN_SUCCESSFUL)) {
                                    loggedIn = true;
                                }
                            } else { // otherwise we consider it bad!
                                responseMessage.setResponse(REQUEST_FAILED_NOT_AUTHENTICATED);
                            }
                        } else {
                            // perform the service being requested, if they're
                            // logged in
                            // we check to see if they want to log out; if they
                            // do, we terminate
                            if (requestMessage instanceof ecologylab.services.messages.Logout) {
                                responseMessage = servicesServer
                                        .performService(requestMessage);

                                // shut down this thread, if the logout was
                                // successful
                                if (responseMessage.getResponse().equals(OK)) {
                                    loggedIn = false;
                                    running = false;
                                }
                            } else {
                                responseMessage = servicesServer
                                        .performService(requestMessage);

                            }
                        }

                        // send the response
                        outputStreamWriter.println(responseMessage
                                .translateToXML(false));
                        outputStreamWriter.flush();
                    }
                }
            } catch (java.net.SocketException e) {
                // this seems to mean the connection went away
                if (outputStreamWriter != null) // dont need the message if
                    // we're already shutting down
                    debug("STOPPING:  It seems we are no longer connected to the client.");
                break;
            } catch (IOException e) {
                // TODO count streak of errors and break;
                debug("IO ERROR: " + e.getMessage());
                e.printStackTrace();
            } catch (XmlTranslationException e) {
                // report error on XML passed through the socket
                debug("Bogus Message ERROR: " + messageString);
                e.printStackTrace();
            }
        }

        synchronized (this) {
            if (!running)
                stop();
        }
    }
}
