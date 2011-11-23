package SeljeIRC;
 
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * @author Lars Erik Pedersen
 *
 * Connects to server
 */


public class ConnectionHandler implements IRCEventListener {
	private ConnectionManager manager;
        
        private IRCEvent event = null;
        private boolean hasConnected = false;
        private tabHandler channelTab;
        private Channel channelCommands;
        private static ConnectionHandler conHandler = new ConnectionHandler();
        
        /* Regular Expression patterns */
        private Pattern inputCommandFinderPattern = Pattern.compile("^/\\w+(\\s#\\w*)?");
        private Pattern inputCommandTextFinderPattern = Pattern.compile("(^/\\w+(\\s#\\w+)?\\s)([\\w\\s]+)?$");
        private Pattern inputCommandChannelPattern = Pattern.compile("#\\w+");	// Finds a channel name in pattern
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        
        private ConnectionHandler(){
 
        	try{
        		channelTab = SeljeIRC.channelTabObj.getInstance();

            channelTab.setConnection(this);
            
            //why? hallvard is asking
            //channelTab.createStatusTab();
            
        	}catch(Exception e){
        		System.err.println(dateFormat.format(date)+" "+I18N.get("connection.systemerror") + e.getMessage());
        	}
            /*
             * created object
             */
                
                
                
                
        }
	 
	public void connectIt(String server, String nicName) {
	
       
		channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.trying"), Colors.statusColor);
		manager = new ConnectionManager(new Profile(nicName));
	
		Session session = manager.requestConnection(server);
		session.addIRCEventListener(this);
	}
	
	public void receiveEvent(IRCEvent e) {
		
            event = e;
            //channelTab.updateStatusScreen("Event :"+e.getType().toString());
            
            if (e.getType() == Type.CONNECT_COMPLETE)
		{   
			
                  channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connect.success"), Colors.statusColor);
                  hasConnected = true;
                        


		}
                else if(e.getType() == Type.CHANNEL_MESSAGE){
                   // Print channel-messages in channel-tab
                    MessageEvent me = (MessageEvent) e;
                    String ch = me.getChannel().getName();
                    
            try {
                channelTab.updateTabScreen(ch, "\n"+dateFormat.format(date), Colors.channelColor);
                channelTab.updateTabScreen(ch, " <"+me.getNick()+">", Colors.nickColor);
                channelTab.updateTabScreen(ch, me.getMessage(), Colors.channelColor);
            } catch (BadLocationException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
            
                else if(e.getType() == Type.PRIVATE_MESSAGE){
                	MessageEvent me = (MessageEvent) e;
                	String userNick = me.getNick();
                	String message = "<" + me.getNick() + ">" + " : " + me.getMessage();
                	
                	if(!channelTab.tabExists(userNick))
                		try {
                             channelTab.createNewTab(userNick, SingleTab.PRIVATE,null);
                        } catch (BadLocationException ex) {
                            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            channelTab.updateTabScreen("\n"+dateFormat.format(date)+" "+userNick, message);
                        } catch (BadLocationException ex) {
                            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }

                            }
                else if(e.getType() == Type.NOTICE){
                        NoticeEvent no = (NoticeEvent) e;
                        
                        String update = no.getNoticeMessage().toString();
                        channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+update, Colors.statusColor);
                }
                else if(e.getType() == Type.ERROR){
                    
                        
                        ErrorEvent no = (ErrorEvent) e;
                        	
                        String update = no.getErrorType().toString();
                        
                        channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+update, Colors.statusColor);
                         //TODO REMOVE
                        channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.errordata") + e.getRawEventData(), Colors.statusColor);
                }
            
                else if(e.getType() == Type.NICK_CHANGE){
                    NickChangeEvent nce = (NickChangeEvent) e;
                    Iterator<Channel> i = e.getSession().getChannels().iterator();
                    String ch;
                    while (i.hasNext())   {                                // If the user that changed nick is in several channels
                        ch = i.next().getName();
                        channelTab.changedNick(nce.getOldNick(), nce.getNewNick(), ch);
                        try {
                            channelTab.updateTabScreen("\n"+dateFormat.format(date)+" "+ch, "-!- " + nce.getOldNick() + I18N.get("connection.knownas") + nce.getNewNick(), Colors.channelColor);
                        } catch (BadLocationException ex) {
                          }
                    }
                }
                
                else if(e.getType() == Type.JOIN_COMPLETE){
                    // Print topic for channel:
                    JoinCompleteEvent jce = (JoinCompleteEvent) e;
                    String ch = jce.getChannel().getName();
                    String message = jce.getChannel().getTopic();
                    try {
                        channelTab.createNewTab(ch,SingleTab.CHANNEL, message);
                        
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
                        channelTab.updateTabScreen(ch, "\n"+dateFormat.format(date)+" "+I18N.get("connection.userl") + message, Colors.channelColor);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                }
                
                else if (e.getType() == Type.NICK_IN_USE)   {
                    NickInUseEvent niu = (NickInUseEvent) e;
                        channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" -!- Nick "+niu.getInUseNick()+ I18N.get("connection.nickinuse"), Colors.statusColor);
                }
                
                else if (e.getType() == Type.JOIN)   {
                    JoinEvent je = (JoinEvent) e;
                    String nick = je.getNick();
                    try {
                        channelTab.updateTabScreen(je.getChannelName(), "\n"+dateFormat.format(date)+" -!- " + nick + I18N.get("channel.userjoin"), Colors.channelColor);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                            channelTab.userJoined(nick,je.getChannelName());
                        }
                
                else if (e.getType() == Type.PART)   {
                    PartEvent pe = (PartEvent) e;
                    String nick = pe.getWho();
                    
                    if(!nick.equalsIgnoreCase(event.getSession().getNick())){
                    
                    try {
                        channelTab.updateTabScreen(pe.getChannelName(), "\n"+dateFormat.format(date)+" -!- " + nick + I18N.get("channel.userleft"), Colors.channelColor );
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                            channelTab.userLeft(nick, pe.getChannelName());
                        
                    }
                }

                else if(e.getType() == Type.MODE_EVENT){
                    // Print mode-adjustments
                    ModeEvent me = (ModeEvent) e;
                    ModeAdjustment m;
                    if (me.getChannel() != null)   {
                        String ch = me.getChannel().getName();
                        String message;
                        StringBuilder strModes = new StringBuilder("-!- mode [");
                        StringBuilder strNicks = new StringBuilder();
                        /*try {
                            channelTab.updateTabScreen(ch, "-!- " + e.getRawEventData());
                        } catch (BadLocationException ex) {
                            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }*/
                        if (me.getModeType() == ModeType.CHANNEL)   {                                   // Voice and Op are channel modes
                            List<ModeAdjustment> modes = me.getModeAdjustments();                       // Get list of adjustments
                            Iterator<ModeAdjustment> i = modes.iterator();
                            while (i.hasNext())   {                                            // Get the first one (there may be more)
                                m = i.next();
                                if (m.getMode() == 'o')   {                                                  // Someone got oped / deoped
                                    channelTab.op(m.getArgument(), m.getAction() == Action.PLUS, ch);
                                    strModes.append(m.toString().substring(0, 2));
                                    strNicks.append(m.getArgument()).append(' ');
                                }
                                else if (m.getMode() == 'v')   {                                                  // Someone got voiced / devoiced
                                    channelTab.voice(m.getArgument(), m.getAction() == Action.PLUS, ch);
                                    strModes.append(m.toString().substring(0, 2));
                                    strNicks.append(m.getArgument()).append(' ');
                                }
                                else
                                    strModes.append(m.toString().substring(0, 2));
                                
                            }
                            if (me.setBy().length() != 0)                                                 
                                message = strModes.toString() + " "  + strNicks.toString() + I18N.get("connection.by") + me.setBy();
                            else
                                message = strModes.toString() + "]";
                            try {
                                channelTab.updateTabScreen(ch, "\n"+dateFormat.format(date)+" "+message, Colors.channelColor);
                            } catch (BadLocationException ex) {
                            }
                        }                            
                    }
                }
                
                else if (e.getType() == Type.CTCP_EVENT)   {
                    CtcpEvent ce = (CtcpEvent) e;
                    if(ce.getCtcpString().equals("VERSION"))   {
                        ce.getSession().notice(ce.getNick(), "\001"+"VERSION SeljeIRC v0.1"+"\001");
                        channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" Version requested from "+ce.getNick(), Colors.statusColor);
                    }
                    else if(ce.getCtcpString().contains("PING"))   {
                         channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" PING from "+ce.getNick(), Colors.statusColor);
                        ce.getSession().notice(ce.getNick(), "\001"+ce.getCtcpString()+"\001");
                    }
                    else if(ce.getCtcpString().contains("TIME"))   {
                        DateFormat df = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss Z");
                        Date date = new Date();
                        ce.getSession().notice(ce.getNick(), "\001"+"TIME "+df.format(date)+"\001");
                         channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" TIME from "+ce.getNick(), Colors.statusColor);
                    }
                    else if (ce.getCtcpString().contains("ACTION"))   {
                        try {
                            channelTab.updateTabScreen(ce.getChannel().getName(), "* "+ce.getNick()+ce.getMessage().substring(7), Colors.channelColor);
                        } catch (BadLocationException ex) {
                        }
                    }
                }

                
                else if(e.getType() == Type.WHOIS_EVENT)   {
                    WhoisEvent we = (WhoisEvent) e;
                    channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" -!- " + we.getUser() + " [" + we.getHost() + "]", Colors.statusColor);
                    channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.ircname") + we.getRealName(), Colors.statusColor);
                    channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.channels") + we.getChannelNames(), Colors.statusColor);
                    channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.server") + we.whoisServer() + " [" + we.whoisServerInfo() + "]", Colors.statusColor);
                    channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.endwhois"), Colors.statusColor);
                    if(we.isIdle()) channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.idle"), Colors.statusColor);
                    else channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.notidle"), Colors.statusColor);
                }
            
                else if(e.getType() == Type.CHANNEL_LIST_EVENT)   {	// Lists all the channels from the servers with topics
                	ChannelListEvent chEvt = (ChannelListEvent) e;
                	channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.channel") + chEvt.getChannelName() + I18N.get("connection.users") + chEvt.getNumberOfUser() + " " + chEvt.getTopic(), Colors.statusColor);
                }
                else if (e.getType() == Type.TOPIC)   {
                    TopicEvent te = (TopicEvent) e;
                    String chanName = te.getChannel().getName();
                    String setBy = te.getSetBy();
                    String[] subSt = setBy.split("~");
                    Date setAt = te.getSetWhen();
                    DateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm:ss zZ");
                    setBy = subSt[0];
                    String topic = te.getTopic();
                    try {
                        
                        channelTab.passTopic(chanName,topic+" "+ I18N.get("connection.setby")+" " +setBy+ " "+I18N.get("connection.at") +" "+df.format(setAt));
                        channelTab.updateTabScreen(chanName, "\n"+dateFormat.format(date)+" -!- " +" "+setBy+" " + I18N.get("connection.changedtopic") +" "+chanName +" "+ I18N.get("connection.to") +" "+ topic, Colors.statusColor);
                    } catch (BadLocationException ex) {
                    }
                }
                
                else if (e.getType() == Type.AWAY_EVENT)   {
                    AwayEvent aw = (AwayEvent) e;
                    String awayUser = aw.getNick();
                    String message = "-!- "+awayUser+ I18N.get("connection.isaway")+aw.getAwayMessage()+"]";
                    int privTabIdx = channelTab.getIndexOfTab(awayUser);
                    if (aw.getEventType() == AwayEvent.EventType.USER_IS_AWAY)   {
                        channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+message, Colors.statusColor);
                        if (privTabIdx > 0)   {
                            try {
                                channelTab.updateTabScreen("\n"+dateFormat.format(date)+" "+awayUser, message, Colors.statusColor);
                            } catch (BadLocationException ex) {
                            }
                        }
                    }
                    if (aw.isYou() && aw.getEventType() == AwayEvent.EventType.WENT_AWAY)   {
                            channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.setaway"), Colors.statusColor);
                    }
                    
                    if(aw.isYou() && aw.getEventType() == AwayEvent.EventType.RETURNED_FROM_AWAY)   {
                            channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.notawayanymore"), Colors.statusColor);
                    }
                    
                }

                else    
		{       // Prints data received from server
                        if(!e.getRawEventData().matches(".*(311|319|312|320|317|318|321|322|323).*")) //Do not print whois events marked as DEFAULT
                        	channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+e.getType() + " " + e.getRawEventData(), Colors.statusColor);
		}
            
        }
        
        
        public void joinChannel (String channel) throws BadLocationException {
           // String tabTitle;
            
            if(connectedToServer()){
                //create a validate function?
                if(!channel.startsWith("#")){		// Appends the hash if not provided
                            StringBuilder stBuild = new StringBuilder();
                            stBuild.insert(0, "#");
                            stBuild.append(channel);
                            channel = stBuild.toString();
                    }
                
                if(channel  != ""){
                    
                        String ch = reWriteChannel(channel);
                        if(ch == null){
                            event.getSession().join(channel);
                        }
                        else{
                            channelTab.setSelectedIndex(channelTab.indexOfTab(ch));
                        }
                }else
                  channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.writeplease"), Colors.statusColor);
            }
            else
                channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.notconnected"), Colors.statusColor);
           
        }
        
        public void createPrivateChat(String userName){
        	if(connectedToServer()){
        		//event.getSession();
        	}else
        		channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.connectfirst"), Colors.statusColor);
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
        		channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.nochannel"), Colors.statusColor);
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
        	this.sayToServer(inputString, null);
        }
        /**
         * Checks commands typed in input field
         * 
         * The ugliest code I've ever written :/
         * 
         * @author Christer Vaskinn
         * @param inputString The field provided from the input field
         * @param channelName The channelname provided by the inputfield
         */
        public void sayToServer(String inputString, String channelName){
        	
        	if(connectedToServer()){
        		
            	Matcher stringCommandFinder = inputCommandFinderPattern.matcher(inputString);  // Finds command and possible channel name
            	channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+inputString, Colors.statusColor);			// Updates statusscreen with the command (no matter what command);
            	
            	if(stringCommandFinder.find()){						// Checks the matcher for commands
            		
            		
            		try{
            			Matcher channelMatcher = inputCommandChannelPattern.matcher(inputString);
            			String commandFromUser = stringCommandFinder.group().toLowerCase();		// Is pretty
            			
            			String textFromCommand = inputString.substring(stringCommandFinder.end());
            			
            			if(channelMatcher.find())		// Finds channel name in command
            				channelName = channelMatcher.group();
            			
            			if(commandFromUser.startsWith("/topic"))				// Sets the channel topic
            				this.setChannelTopic(channelName, textFromCommand);
            			else if(commandFromUser.startsWith("/j") || commandFromUser.startsWith("/join")){	// Joins channel Require hashtag
            				joinChannel(channelName);
            			}
            			else if(commandFromUser.startsWith("/op")){							// Set op in the channel
            				Channel ch = event.getSession().getChannel(channelName);
            				ch.op(textFromCommand);
            			}
            			else if(commandFromUser.startsWith("/deop")){						// Set deop in the channel
            				Channel ch = event.getSession().getChannel(channelName);
            				ch.deop(textFromCommand);
            			}
            			else if(commandFromUser.startsWith("/voice")){						// Set voice in the channel
            				Channel ch = event.getSession().getChannel(channelName);
            				ch.voice(textFromCommand);
            			}
            			else if(commandFromUser.startsWith("/devoice")){					// Set devoice in the channel
            				Channel ch = event.getSession().getChannel(channelName);
            				ch.deVoice(textFromCommand);
            			}
            			else if(commandFromUser.startsWith("/mode")){						// Set mode in the channel
            				Channel ch = event.getSession().getChannel(channelName);
            				ch.mode(textFromCommand);										
            			}
                                else if (commandFromUser.startsWith("/away")){                                          // Set or unset away
                                    if (!textFromCommand.equals(""))
                                        event.getSession().setAway(textFromCommand);
                                    else
                                        event.getSession().unsetAway();
                                }
            			else if(commandFromUser.startsWith("/raw")){						// Send Raw in the irc
            				event.getSession().sayRaw(inputString);
            			}

            			else if(commandFromUser.startsWith("/quit")){						// Quit from server
            				System.exit(0);
            			}
                                else if(commandFromUser.startsWith("/nick")){
                                    event.getSession().changeNick(textFromCommand);
                                }
            			else if(commandFromUser.startsWith("/help")){						// Help commands
            				String allowedCommands = I18N.get("connection.allowedcommands");
            				if(channelName != null)
            					channelTab.updateTabScreen("\n"+dateFormat.format(date)+" "+channelName, allowedCommands, Colors.channelColor);
            				else
            					channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+allowedCommands, Colors.statusColor);
            			}
            			else if(commandFromUser.startsWith("/disconnect")){					// Disconnect from server
            				this.closeConnection();
            			}
            			else
            				channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.whyusostupid"), Colors.statusColor);
            			
            		}catch(Exception e){
            			channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.exception") + e.getMessage(), Colors.statusColor);
            		}
            } else {														// If not connected to server
                channelTab.updateStatusScreen(dateFormat.format(date)+" "+inputString + "\n" +dateFormat.format(date)+" "+I18N.get("connection.notconnected"), Colors.statusColor);

            }
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
            	channelTab.updateTabScreen(channel, "\n"+dateFormat.format(date), Colors.channelColor);
                channelTab.updateTabScreen(channel, " <"+event.getSession().getNick()+"> ", Colors.nickColor);
                channelTab.updateTabScreen(channel, whatToSay, Colors.channelColor);
            }catch(BadLocationException ex){
            	System.err.println(I18N.get("\n"+dateFormat.format(date)+" "+"connection.privmsgerror") + ex.getMessage());
            }
            
        }
        
        public void sayToPrivate(final String whatToSay, final String nickName){
        	try{
        		event.getSession().sayPrivate(nickName, whatToSay);
        	
        		channelTab.updateTabScreen(nickName, "\n"+dateFormat.format(date)+" <"+event.getSession().getNick()+"> " +whatToSay, Colors.channelColor);
        	}catch(Exception ex){
        		System.err.println("\n"+dateFormat.format(date)+" "+I18N.get("connection.privmsgerror") + ex.getMessage());
        	}
        }
        
        public void closeConnection(){
            manager.quit();
            channelTab.removeAllTabs();
            System.out.printf("\n"+dateFormat.format(date)+" "+I18N.get("connection.close"));
        }
        /**
         * Get's all the channels from the server
         * 
         * @author Christer Vaskinn
         * @note This function is really fucking stupid and using it on the major networks will get your ass K-lined(banned)
         */
        public void getAllTheChannelsFromServer(){
        	if(this.connectedToServer()){
        		channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.list"), Colors.statusColor);
        		event.getSession().chanList();
			}else
        		channelTab.updateStatusScreen("\n"+dateFormat.format(date)+" "+I18N.get("connection.notconnected"), Colors.statusColor);
        }
        
        public void disconnectFromChannel(String channel){
            
            //TODO Final event comes after part and creates exception because its routed to tab and not statusscreen
            event.getSession().getChannel(channel).part(channel);
            
        }
        
       
        public Session getCurrentSession()   {
            return event.getSession();
        }
        
        public void validateChannel(String ch){
            
        }
        
        public String reWriteChannel(String ch){
            
            List<Channel> l = event.getSession().getChannels();
            Iterator<Channel> i = l.iterator();
            String ls;
            while(i.hasNext())   {
                ls = i.next().getName();
                if(ls.equalsIgnoreCase(ch))
                    return ls;
            }
            return null;
        }

		public static ConnectionHandler getInstance() {
			return conHandler;
		}
            
        
        
	
} // End of public class ConnectionHandler	

