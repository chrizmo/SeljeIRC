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
	
	public ConnectToServer(String server, String nicName) {
	
	manager = new ConnectionManager(new Profile(nicName));
	
	Session session = manager.requestConnection(server);
	session.addIRCEventListener(this);
	}

	
	public void receiveEvent(IRCEvent e) {
		if (e.getType() == Type.CONNECT_COMPLETE)
		{   
			System.out.println("*** Successfully connected to server ***");
			
			//e.getSession().join("#SeljeIRC");
		}

		else
		{       // Prints data received from server
			System.out.println(e.getType() + " " + e.getRawEventData());
		}
		e.getRawEventData();

	}  // End of public void receiveEvent
        public void joinChannel (IRCEvent e, String channel) {

            e.getSession().join(channel);
        }
	
	
} // End of public class ConnectToServer	

