package SeljeIRC;
 
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        private Channel channelCommands;
        
        
        /* Regular Expression patterns */
        private Pattern inputCommandFinderPattern = Pattern.compile("^/\\w+(\\s#\\w*)?");
        private Pattern inputCommandTextFinderPattern = Pattern.compile("(^/\\w+(\\s#\\w+)?\\s)([\\w\\s]+)?$");
        private Pattern inputCommandChannelPattern = Pattern.compile("#\\w+");	// Finds a channel name in pattern
        
        
        public ConnectionHandler(tabHandler ct){
 
        	try{
        		channelTab = ct;

        		channelTab.setConnection(this);
        		channelTab.createStatusTab();
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
                        channelTab.updateStatusScreen("Error data: " + e.getRawEventData()); //TODO REMOVE
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
            try {
                channelTab.updateTabScreen(ch, message);
            } catch (BadLocationException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
                    channelTab.fetchUsers(ch, e.getSession().getChannel(ch));

                }
                else if(e.getType() == Type.NICK_LIST_EVENT){
                    // List users in channel:
                    NickListEvent nle = (NickListEvent) e;
                    String ch = nle.getChannel().getName();
                    List<String> message = nle.getNicks();
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

                else    
		{       // Prints data received from server
                        channelTab.updateStatusScreen(e.getType() + " " + e.getRawEventData());
			
		}
            
        }
        
        
        public void joinChannel (String channel) throws BadLocationException {
            try {
            if(channel != null){
            	if(connectedToServer()){		// TODO: Sjekke om man alt har koblet til kanal            		
            		event.getSession().join(channel);
            		channelTab.updateTabScreen(channel, "-!- You have joined :"+channel);
            	}
            	else
            		channelTab.updateStatusScreen("You have to connect to server first");
        	}else{
        			channelTab.updateStatusScreen("Channel not provided");
        	}
            }catch (Exception ex){
            	System.err.println("Exception caught! Type: " + ex.getClass().toString() + " Message: " + ex.getMessage());
            }
        	
        }
        
        public void createPrivateChat(String userName){
        	if(connectedToServer()){
        		//event.getSession();
        	}else
        		channelTab.updateStatusScreen("You have to connect to server first");
        }
        
        /**
         * Sets the channel topic for a specified channel
         * 
         * @author Christer Vaskinn
         * @param channelName String, the channel to set the topic at
         * @param channelTopic String, the topic to set on the channel
         */
        
        public void setChannelTopic(String channelName, String channelTopic){
        	if(channelName != null){
        			Channel ircChannel =  event.getSession().getChannel(channelName);
        			ircChannel.setTopic(channelTopic);
        	}else{
        		channelTab.updateStatusScreen("Channel name not provided");
        	}
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
                
        public void sayToServer(String inputString){
                if(connectedToServer()){
            	
            	Matcher stringCommandFinder = inputCommandFinderPattern.matcher(inputString);
            	
            	
            	channelTab.updateStatusScreen(inputString);
            	
            	if(stringCommandFinder.find()){
            		String channelName = null;
            		
            		//inputString = inputString.substring(1);
            		//this.setChannelTopic(channelName, inputString);
            		
            		try{
            			Matcher channelMatcher = inputCommandChannelPattern.matcher(inputString);
            			String commandFromUser = stringCommandFinder.group().toLowerCase();
            			
            			String textFromCommand = inputString.substring(stringCommandFinder.end());
            			
            			if(channelMatcher.find())
            				channelName = channelMatcher.group();
            			
            			if(commandFromUser.startsWith("/topic"))
            				this.setChannelTopic(channelName, textFromCommand);
            			else if(commandFromUser.startsWith("/j") || commandFromUser.startsWith("/join"))
            				//System.err.println(channelName);
            				channelTab.createNewTab(channelName, SingleTab.CHANNEL);
            			//else if(commandFromUser.startsWith("/msg")) // FIX: legge inn dette?
            			else if(commandFromUser.startsWith("/op")){
            				Channel ch = event.getSession().getChannel(channelName);
            				ch.op(textFromCommand);
            			}
            			else if(commandFromUser.startsWith("/deop")){
            				Channel ch = event.getSession().getChannel(channelName);
            				ch.deop(textFromCommand);
            			}
            			else if(commandFromUser.startsWith("/voice")){
            				Channel ch = event.getSession().getChannel(channelName);
            				ch.voice(textFromCommand);
            			}
            			else if(commandFromUser.startsWith("/devoice")){
            				Channel ch = event.getSession().getChannel(channelName);
            				ch.deVoice(textFromCommand);
            			}
            			else if(commandFromUser.startsWith("/mode")){
            				Channel ch = event.getSession().getChannel(channelName);
            				ch.mode(textFromCommand);
            			}
            			else if(commandFromUser.startsWith("/raw")){
            				event.getSession().sayRaw(inputString);
            			}
            			else
            				channelTab.updateStatusScreen("Y U NO PROVIDE CORRECT COMMAND");
            			
            		}catch(Exception e){
            			System.err.println("Exception caught: " + e.getMessage());
            		}
            	}
            }else{
                channelTab.updateStatusScreen(inputString + "\n" + "Not connected to server tough");
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
            
            event.getSession().close(channel);
            
        }
        public Session getCurrentSession()   {
            return event.getSession();
        }
        
	
} // End of public class ConnectionHandler	

