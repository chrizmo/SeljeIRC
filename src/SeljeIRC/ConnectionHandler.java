package SeljeIRC;
 
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import jerklib.Channel;
import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.events.*;
import jerklib.events.IRCEvent.Type;
import jerklib.events.modes.ModeAdjustment;
import jerklib.events.modes.ModeAdjustment.Action;
import jerklib.events.modes.ModeEvent;
import jerklib.events.modes.ModeEvent.ModeType;
import jerklib.listeners.IRCEventListener;


/**
 * 
 * @author Jon Arne Westgaard
 */


public class ConnectionHandler implements IRCEventListener {
	private ConnectionManager manager;
        
        private IRCEvent event = null;
        private boolean hasConnected = false;
        private tabHandler channelTab;
        
        
        
        public ConnectionHandler(tabHandler ct){
 
        	try{
        		channelTab = ct;

            channelTab.setConnection(this);
            
            
            //why? hallvard is asking
            //channelTab.createStatusTab();
            
        	}catch(Exception e){
        		System.err.println("System error" + e.getMessage());
        	}
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
            try {
                channelTab.updateTabScreen(ch, message);
            } catch (BadLocationException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
            
                else if(e.getType() == Type.PRIVATE_MESSAGE){
                	MessageEvent me = (MessageEvent) e;
                	String userNick = me.getNick();
                	String message = "<" + me.getNick() + ">" + " : " + me.getMessage();
                	
                	if(!channelTab.tabExists(userNick))
                		channelTab.createNewTab(userNick, SingleTab.PRIVATE);
            try {
                channelTab.updateTabScreen(userNick, message);
            } catch (BadLocationException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
                	
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
                	//ErrorEvent no = (ErrorEvent) e;
                	//String update = no.getErrorType().toString();
                	//channelTab.updateStatusScreen("NickInUseBuddy");
                    //channelTab.updateStatusScreen(update);
                    NickChangeEvent nce = (NickChangeEvent) e;
                    Iterator<Channel> i = e.getSession().getChannels().iterator();
                    String ch;
                    while (i.hasNext())   {                                // If the user that changed nick is in several channels
                        ch = i.next().getName();
                        channelTab.changedNick(nce.getOldNick(), nce.getNewNick(), ch);
                        try {
                            channelTab.updateTabScreen(ch, "-!- " + nce.getOldNick() + " is known as " + nce.getNewNick()); //TODO I18N
                        } catch (BadLocationException ex) {
                          }
                    }
                }
                
                else if(e.getType() == Type.JOIN_COMPLETE){
                    // Print topic for channel:
                    JoinCompleteEvent jce = (JoinCompleteEvent) e;
                    String ch = jce.getChannel().getName();
                    String message = ("-!- Topic for " +ch +": "+jce.getChannel().getTopic());
                    try {
                        channelTab.updateTabScreen(ch, message);
                    } catch (BadLocationException ex) {
                    }

                }
                else if(e.getType() == Type.NICK_LIST_EVENT){
                    // List users in channel:
                    NickListEvent nle = (NickListEvent) e;
                    String ch = nle.getChannel().getName();
                    List<String> message = nle.getNicks();
                    channelTab.fetchUsers(ch, e.getSession().getChannel(ch));
                    try {
                        channelTab.updateTabScreen(ch, "-!- Users: " + message);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                }
                
                else if (e.getType() == Type.JOIN)   {
                    JoinEvent je = (JoinEvent) e;
                    String nick = je.getNick();
            try {
                channelTab.updateTabScreen(je.getChannelName(), "-!- " + nick + I18N.get("channel.userjoin"));
            } catch (BadLocationException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
                    channelTab.userJoined(nick,je.getChannelName());
                }
                
                else if (e.getType() == Type.PART)   {
                    PartEvent pe = (PartEvent) e;
                    String nick = pe.getWho();
            try {
                channelTab.updateTabScreen(pe.getChannelName(), "-!- " + nick + I18N.get("channel.userleft"));
            } catch (BadLocationException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
                    channelTab.userLeft(nick, pe.getChannelName());
                }

                else if(e.getType() == Type.MODE_EVENT){
                    // Print mode-adjustments
                    ModeEvent me = (ModeEvent) e;
                    if (me.getChannel() != null)   {
                        String ch = me.getChannel().getName();
                try {
                    channelTab.updateTabScreen(ch, "-!- " + e.getRawEventData());
                } catch (BadLocationException ex) {
                    Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                        if (me.getModeType() == ModeType.CHANNEL)   {                                   // Voice and Op are channel modes
                            List<ModeAdjustment> modes = me.getModeAdjustments();                       // Get list of adjustments
                            Iterator<ModeAdjustment> i = modes.iterator();
                            while (i.hasNext())   {                                            // Get the first one (there may be more)
                                ModeAdjustment m = i.next();
                                if (m.getMode() == 'o')                                                     // Someone got oped / deoped
                                    channelTab.op(m.getArgument(), m.getAction() == Action.PLUS, ch);
                                if (m.getMode() == 'v')                                                     // Someone got voiced / devoiced
                                    channelTab.voice(m.getArgument(), m.getAction() == Action.PLUS, ch); 
                            }
                               
                        }                            
                    }
                }
                
                else if (e.getType() == Type.CTCP_EVENT)   {
                    CtcpEvent ce = (CtcpEvent) e;
                    if(ce.getCtcpString().equals("VERSION"))   {
                        ce.getSession().notice(ce.getNick(), "\001"+"VERSION SeljeIRC v0.1"+"\001");
                    }
                    else if(ce.getCtcpString().contains("PING"))   {
                        System.out.println(ce.getCtcpString());
                        ce.getSession().notice(ce.getNick(), "\001"+ce.getCtcpString()+"\001");
                    }
                    else if(ce.getCtcpString().contains("TIME"))   {
                        DateFormat df = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss Z");
                        Date date = new Date();
                        ce.getSession().notice(ce.getNick(), "\001"+"TIME "+df.format(date)+"\001");
                    }
                }
                
                else if(e.getType() == Type.WHOIS_EVENT)   {
                    WhoisEvent we = (WhoisEvent) e;
                    channelTab.updateStatusScreen("-!- " + we.getUser() + " [" + we.getHost() + "]");
                    channelTab.updateStatusScreen("-!-  ircname   : " + we.getRealName());
                    channelTab.updateStatusScreen("-!-  channels  : " + we.getChannelNames());
                    channelTab.updateStatusScreen("-!-  server    : " + we.whoisServer() + " [" + we.whoisServerInfo() + "]");
                    channelTab.updateStatusScreen("-!- End of WHOIS");
                }

                else    
		{       // Prints data received from server
                        System.out.print(e.getRawEventData());
                        if(!e.getRawEventData().matches(".*(311|319|312|320|317|318).*")) //Do not print whois events marked as DEFAULT
                        channelTab.updateStatusScreen(e.getType() + " " + e.getRawEventData());
		}
            
        }
        
        
        public void joinChannel (String channel) throws BadLocationException {
            
            
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
        public void sayToChannel(String whatToSay,String channel) throws BadLocationException{
            
            try{
            	event.getSession().sayChannel(whatToSay,event.getSession().getChannel(channel));
            /*
             * just so you see what you write
             */
            	channelTab.updateTabScreen(channel, "<"+event.getSession().getNick()+"> " +whatToSay);
            }catch(BadLocationException ex){
            	System.err.println("Error sending private message: " + ex.getMessage());
            }
            
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
            channelTab.removeAllTabs();
            System.out.printf("Closing manager");
        }
        
        public void disconnectFromChannel(String channel){
            
            //TODO Final event comes after part and creates exception because its routed to tab and not statusscreen
            event.getSession().getChannel(channel).part(channel);
            
        }
        public Session getCurrentSession()   {
            return event.getSession();
        }
        
	
} // End of public class ConnectionHandler	

