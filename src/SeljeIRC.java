import SeljeIRC.IrcLayout;
import javax.swing.*;


/**
 * 
 * @author Hallvard Westman
 */

public class SeljeIRC extends JFrame{

public static void main(String[] args) {
    
       IrcLayout irc = new IrcLayout();
       irc.createLayout();
       JFrame f = new JFrame();
       
       
     
       f.getContentPane().add(irc);
       
       f.setVisible(true);
       f.pack();
       
       f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
       
        
 }
}