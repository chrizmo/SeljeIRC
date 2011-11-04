package SeljeIRC;
 
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.events.*;
import jerklib.events.IRCEvent.Type;
import jerklib.listeners.IRCEventListener;

public class ConnectToServer implements IRCEventListener {
	private ConnectionManager manager;
 
	public ConnectToServer() {
		/* ConnectionManager takes a Profile to use for new connections. */
	manager = new ConnectionManager(new Profile("JADDA"));
		/* ConnectionManager#requestConnection(String) will return a Session object.
		 * The Session is the main way users will interact with this library and IRC
		 * networks */
	Session session = manager.requestConnection("irc.homelien.no");
 
		/* JerkLib fires IRCEvents to notify users of the lib of incoming events
		 * from a connected IRC server. */
		session.addIRCEventListener(this);
	}
 
	/*
	 * This method is for implementing an IRCEventListener. This method will be
	 * called anytime Jerklib parses an event from the Session its attached to.
	 * All events are sent as IRCEvents. You can check its actual type and cast it
	 * to a more specific type.
	 */
	public void receiveEvent(IRCEvent e) {
		if (e.getType() == Type.CONNECT_COMPLETE)
		{
			e.getSession().join("#SeljeIRC");
 
		}
		else if (e.getType() == Type.CHANNEL_MESSAGE)
		{
			MessageEvent me = (MessageEvent) e;
			System.out.println("<" + me.getNick() + ">"+ ":" + me.getMessage());
		}
		else if (e.getType() == Type.JOIN_COMPLETE)
		{
			JoinCompleteEvent jce = (JoinCompleteEvent) e;
 
				/* Prepare to read text from keyboard*/
			InputStreamReader istream = new InputStreamReader(System.in) ;
	        BufferedReader bufRead = new BufferedReader(istream) ;
	        String kommando = null;
			do {
	        try {
				kommando = bufRead.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			/* Say something nice: */
	        jce.getChannel().say(kommando);
			}
			/* For now we'll just loop forever, just to see that we are able to write a bunch of text... */
			while (true);
		}
		else
		{
			System.out.println(e.getType() + " " + e.getRawEventData());
		}
	}
 
	public static void main(String[] args) {
		new ConnectToServer();
	}
}