package SeljeIRC;
 
import java.io.BufferedReader; 	  // Will be used later. I guess...
import java.io.IOException;		  // Will be used later. I guess...
import java.io.InputStreamReader; // Will be used later. I guess...
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.events.*;
import jerklib.events.IRCEvent.Type;
import jerklib.listeners.IRCEventListener;


/**
 * 
 * @author Jon Arne Westgaard
 */


public class ConnectToServer implements IRCEventListener {
	private ConnectionManager manager;
	
	public ConnectToServer() {
	// TODO Should be <options.get("Username", "")> etc for username, nick, alternate
	manager = new ConnectionManager(new Profile("SeljeIRC"));
	// TODO Should be <options.get("server");> for server and port
	Session session = manager.requestConnection("irc.homelien.no");
	session.addIRCEventListener(this);
	}
	
	public void receiveEvent(IRCEvent e) {
		if (e.getType() == Type.CONNECT_COMPLETE)
		{   
			/* When connected, join the specified channel
			 * TODO  Should be <options.get(#Channel#);>   */
			e.getSession().join("#SeljeIRC");
		}
		 

		else if (e.getType() == Type.CHANNEL_MESSAGE)
		{
            JoinCompleteEvent jce = (JoinCompleteEvent) e;
			MessageEvent me = (MessageEvent) e;
			System.out.println("<" + me.getNick() + ">"+ ":" + me.getMessage());
		}

		
		else if (e.getType() == Type.JOIN_COMPLETE)
		{
			JoinCompleteEvent jce = (JoinCompleteEvent) e;
			jce.getChannel().say("Let's say something nice now that we have joined the channel");
		}

		else
		{
			System.out.println(e.getType() + " " + e.getRawEventData());
		}

		
	}  // End of public void receiveEvent
	
	public static void main(String [] args) {
		new ConnectToServer();
	}
	
	
} // End of public class ConnectToServer	

