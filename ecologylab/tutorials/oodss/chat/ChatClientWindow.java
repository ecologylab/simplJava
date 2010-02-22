package ecologylab.tutorials.oodss.chat;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import ecologylab.collections.Scope;
import ecologylab.services.distributed.client.NIOClient;
import ecologylab.services.distributed.exception.MessageTooLargeException;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.TranslationScope;

/**
 * ChatClientWindow implements a basic chat client. Attaches to
 * PublicChatServer, which upon receipt of chat messages updates all attached
 * clients of the message.
 * 
 * @author bill
 */
public class ChatClientWindow extends JFrame implements ChatUpdateListener,
		ActionListener, WindowListener
{
	public static String	serverAddress	= "localhost";

	public static int		portNumber		= 2108;

	private JTextArea		echoArea;

	private JTextField	entryField;

	private NIOClient		client;

	public ChatClientWindow(NIOClient client, Scope scope)
	{
		/*
		 * Set the window as the listener for chat updates in the application
		 * scope. This ensures that recievedUpdate will be called when incoming
		 * updates are recieved.
		 */
		scope.put(ChatUpdateListener.CHAT_UPDATE_LISTENER, this);

		/*
		 * Store the client instance so that we can send messages later.
		 */
		this.client = client;

		/*
		 * Set's up the swing interface and add's this as an ActionListener to
		 * the Send Message button.
		 */
		setupSwingComponents();
	}

	@Override
	public void recievedUpdate(ChatUpdate response)
	{
		/*
		 * We received an chat update message so we post the message in the text
		 * area.
		 */
		echoArea.insert(response.getHost() + ":" + response.getPort() + "->"
				+ response.getMessage() + "\n\n", 0);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		/* send message button pushed */
		String message = entryField.getText();
		ChatRequest request = new ChatRequest(message);

		try
		{
			/*
			 * Send request to post message to chat server.
			 */
			client.sendMessage(request);

			/*
			 * Update personal text area to reflect sent message.
			 */
			echoArea.insert("---------me------->" + message + "\n\n", 0);
		}
		catch (MessageTooLargeException e1)
		{
			System.err.println("Failed to send message because it was too large: "
					+ entryField.getText());
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException
	{
		/*
		 * Get chat translations with static accessor
		 */
		TranslationScope publicChatTranslations = ChatTranslations.get();

		Scope clientScope = new Scope();

		NIOClient client = new NIOClient(serverAddress, portNumber,
				publicChatTranslations, clientScope);

		/*
		 * Enable compression and connect the client to the server.
		 */
		client.allowCompression(true);
		client.useRequestCompression(true);
		client.connect();

		/*
		 * If the client connects, start chat window to run the application. Pass
		 * in client and clientScope instance.
		 */
		if (client.connected())
		{
			ChatClientWindow window = new ChatClientWindow(client, clientScope);
		}
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		/*
		 * disconnect and close
		 */
		client.disconnect(true);
		System.exit(0);
	}

	private void setupSwingComponents()
	{
		this.setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane();

		echoArea = new JTextArea();
		echoArea.setBorder(new EtchedBorder());
		echoArea.setEditable(false);

		scrollPane.setViewportView(echoArea);
		scrollPane.setAutoscrolls(true);
		this.add(scrollPane, BorderLayout.CENTER);

		JPanel entryPanel = new JPanel();
		entryPanel.setLayout(new BorderLayout());

		entryField = new JTextField();
		JButton sendButton = new JButton("Send Message");
		sendButton.addActionListener(this);
		entryPanel.add(entryField, BorderLayout.CENTER);
		entryPanel.add(sendButton, BorderLayout.EAST);

		this.add(entryPanel, BorderLayout.SOUTH);
		this.setTitle("Chat Client");
		this.setSize(400, 400);
		this.addWindowListener(this);
		this.setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

}