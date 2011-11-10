package SeljeIRC;
 
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
        
        private IRCEvent event = null;
        private boolean hasConnected = false;
	
	public ConnectToServer(String server, String nicName) {
	
	manager = new ConnectionManager(new Profile(nicName));
	
	Session session = manager.requestConnection(server);
	session.addIRCEventListener(this);
	}

	
	public void receiveEvent(IRCEvent e) {
		
            event = e;
            
            if (e.getType() == Type.CONNECT_COMPLETE)
		{   
			System.out.println(I18N.get("connect.success"));
			hasConnected = true;

                        
		}

            else if (e.getType() == Type.CHANNEL_MESSAGE)
	        {
	        MessageEvent me = (MessageEvent) e;
                System.out.println(me.getNick() + ":" + me.getMessage());
                //me.getChannel().say("Modes :" + me.getChannel().getUsersModes(me.getNick()).toString());

                
	        }
		else
		{       // Prints data received from server
			System.out.println(e.getType() + " " + e.getRawEventData());
                        // TODO Send this to Status-window...
		}
        }
        
        public void joinChannel (String channel) {
            
            
            if(connectedToServer())
                event.getSession().join(channel);
            else
                System.out.println("Not connected to server yet");
        }
	
        public boolean connectedToServer(){
            
            if(event != null){
                if(hasConnected)
                    return true;
                else
                    return false;
                
            }
            else
                return false;
        }
	
} // End of public class ConnectToServer	

