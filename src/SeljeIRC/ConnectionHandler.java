package SeljeIRC;
 
import java.awt.Color;
import java.util.List;
import jerklib.Channel;
import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.events.*;
import jerklib.events.IRCEvent.Type;
import jerklib.events.modes.ModeEvent;
import jerklib.listeners.IRCEventListener;


/**
 * 
 * @author Jon Arne Westgaard
 */


public class ConnectionHandler implements IRCEventListener {
	private ConnectionManager manager;
        
        private IRCEvent event = null;
        private boolean hasConnected = false;
        private ChannelTab channelTab;
        
        
        
        public ConnectionHandler(ChannelTab ct){
            channelTab = ct;
            
            channelTab.setConnection(this);
            channelTab.createStatusTab();
            /*
             * created object
             */
        }
	
	public void connectIt(String server, String nicName) {
	
       
		channelTab.updateStatusScreen("Trying to establish connection");    
		manager = new ConnectionManager(new Profile(nicName));
	
		Session session = manager.requestConnection(server);
		session.addIRCEventListener(this);
	}

	
	public void receiveEvent(IRCEvent e) {
		
            event = e;
            
            
            //channelTab.updateStatusScreen("Event :"+e.getType().toString());
            
            if (e.getType() == Type.CONNECT_COMPLETE)
		{   
			
                        channelTab.updateStatusScreen(I18N.get("connect.success"));
                        hasConnected = true;
                        


		}
                else if(e.getType() == Type.CHANNEL_MESSAGE){
                   // Print channel-messages in channel-tab
                    MessageEvent me = (MessageEvent) e;
                    
                    String ch = me.getChannel().getName();
                    String message = "<"+me.getNick()+">" +" : "+me.getMessage();
                    channelTab.updateTabScreen(ch,message);
                }
            
                else if(e.getType() == Type.PRIVATE_MESSAGE){
                	MessageEvent me = (MessageEvent) e;
                	String userNick = me.getNick();
                	String message = "<" + me.getNick() + ">" + " : " + me.getMessage();
                	
                	if(!channelTab.tabExists(userNick))
                		channelTab.createNewTab(userNick, SingleTab.PRIVATE);
                	
            		channelTab.updateTabScreen(userNick, message);
                	
                }
                else if(e.getType() == Type.NOTICE){
                        NoticeEvent no = (NoticeEvent) e;
                        
                        String update = no.getNoticeMessage().toString();
                        channelTab.updateStatusScreen(update);
                }
                else if(e.getType() == Type.ERROR){
                    
                        
                        ErrorEvent no = (ErrorEvent) e;
                        
                        String update = no.getErrorType().toString();
                        channelTab.updateStatusScreen(update);
                }
            
                else if(e.getType() == Type.NICK_CHANGE){
                	ErrorEvent no = (ErrorEvent) e;
                	String update = no.getErrorType().toString();
                	channelTab.updateStatusScreen("NickInUseBuddy");
                    channelTab.updateStatusScreen(update);
                }
                else if(e.getType() == Type.JOIN_COMPLETE){
                    // Print topic for channel:
                    JoinCompleteEvent jce = (JoinCompleteEvent) e;
                    String ch = jce.getChannel().getName();
                    String message = ("-!- Topic for " +ch +": "+jce.getChannel().getTopic());
                    channelTab.updateTabScreen(ch,message);
                    channelTab.fetchUsers(ch, e.getSession().getChannel(ch));

                }
                else if(e.getType() == Type.NICK_LIST_EVENT){
                    // List users in channel:
                    NickListEvent nle = (NickListEvent) e;
                    String ch = nle.getChannel().getName();
                    List<String> message = nle.getNicks();
                    channelTab.updateTabScreen(ch, "-!- Users: " +message);

                }

                else if(e.getType() == Type.MODE_EVENT){
                    // Print mode-adjustments
                    ModeEvent me = (ModeEvent) e;

                    String ch = me.getChannel().getName();
                    channelTab.updateTabScreen(ch, "-!- " +e.getRawEventData());

                }


                else    
		{       // Prints data received from server
                        channelTab.updateStatusScreen(e.getType() + " " + e.getRawEventData());
			
		}
            
        }
        
        
        public void joinChannel (String channel) {
            
            
            if(connectedToServer()){
                event.getSession().join(channel);
                channelTab.updateTabScreen(channel, "-!- You have joined :"+channel);
            }
            else
                channelTab.updateStatusScreen("You have to connect to server first");
        }
        
        public void createPrivateChat(String userName){
        	if(connectedToServer()){
        		//event.getSession();
        	}else
        		channelTab.updateStatusScreen("You have to connect to server first");
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
        /*
         * sending string directly to server
         */
                
        public void sayToServer(String whatToSay){
                
            if(connectedToServer()){
                event.getSession().sayRaw(whatToSay);
            
                channelTab.updateStatusScreen(whatToSay);
            }
            else{
                channelTab.updateStatusScreen(whatToSay + "\n" + "Not connected to server tough");
            }
                
                
        }
        /*
         * sending string to channel
         */
        public void sayToChannel(String whatToSay,String channel){
            
            
            event.getSession().sayChannel(whatToSay,event.getSession().getChannel(channel));
            /*
             * just so you see what you write
             */
            channelTab.updateTabScreen(channel, "<"+event.getSession().getNick()+"> " +whatToSay);
            
        }
        
        public void sayToPrivate(final String whatToSay, final String nickName){
        	try{
        		event.getSession().sayPrivate(nickName, whatToSay);
        	
        		channelTab.updateTabScreen(nickName, "<"+event.getSession().getNick()+"> " +whatToSay);
        	}catch(Exception ex){
        		System.err.println("Error sending private message: " + ex.getMessage());
        	}
        }
        
        public void closeConnection(){
            manager.quit();
            channelTab.removeAll();
            System.out.printf("Closing manager");
        }
        
        public void disconnectFromChannel(String channel){
            
            event.getSession().close(channel);
            
        }
        public Session getCurrentSession()   {
            return event.getSession();
        }
        
	
} // End of public class ConnectionHandler	

