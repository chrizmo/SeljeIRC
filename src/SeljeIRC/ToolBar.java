/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

/**
 *
 * @author wbserver
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * The toolbar for application, takes care of GUI
 * @author hallvardwestman
 */
public class ToolBar extends JToolBar {
    
    private MainMenu mainMenu = SeljeIRC.mainMenu;
    
    // Creates buttons with icons
    private ToolbarButton newConnectionButton = new ToolbarButton(
            new ImageIcon("src/images/SeljeIRC-icons/32x32/link.png"));
    private ToolbarButton newChannelButton = new ToolbarButton(
            new ImageIcon("src/images/SeljeIRC-icons/32x32/add.png"));
    private ToolbarButton getChannelButton = new ToolbarButton(
            new ImageIcon("src/images/SeljeIRC-icons/32x32/notebook.png"));
    private ToolbarButton colorsFontButton = new ToolbarButton(
            new ImageIcon("src/images/SeljeIRC-icons/32x32/paint_brush.png"));
    private ToolbarButton changeNickButton = new ToolbarButton(
            new ImageIcon("src/images/SeljeIRC-icons/32x32/user.png"));
    private ToolbarButton setTopic = new ToolbarButton(
            new ImageIcon("src/images/SeljeIRC-icons/32x32/comment.png"));
    private ToolbarButton helpButton = new ToolbarButton(
            new ImageIcon("src/images/SeljeIRC-icons/32x32/heart.png"));
    
    private Component frame;
    
   
    
    /**
     * INITIATES toolbar
     */
    public ToolBar(){
        super();
        initTB();
        addButtons();
     
    }

    /**
     * sets GUI-settings
     */
    private void initTB () {
        setFloatable(false);   /* Removes floatable toolbar*/
        setRollover(true);
        setOrientation(HORIZONTAL);
        
    }
    
    /**
     * addButtons - Set options and actions for the buttons 
     * and add them to the toolbar
     */
    private void addButtons() {
        
        newConnectionButton.setMnemonic('C');
        newConnectionButton.setToolTipText(I18N.get("mainmenu.newserver"));
        newConnectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)   {
                mainMenu.newConnection();
            }
        });
         

        add(newConnectionButton);

        newChannelButton.setMnemonic('N');
        newChannelButton.setToolTipText(I18N.get("mainmenu.newchannel"));
        newChannelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)   {
           
                mainMenu.newChannel();
            }
        });
        add(newChannelButton);
        addSeparator();
        
        getChannelButton.setMnemonic('G');
        getChannelButton.setToolTipText(I18N.get("mainmenu.getchlist"));
        getChannelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)   {
                mainMenu.getChannels();
            }
        });
        add(getChannelButton);
        
        
        colorsFontButton.setMnemonic('F');
        colorsFontButton.setToolTipText(I18N.get("colors.andfonts"));
        colorsFontButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)   {
                mainMenu.getColorChoices();
            }
        });
        add(colorsFontButton);
       
        changeNickButton.setMnemonic('U');
        changeNickButton.setToolTipText(I18N.get("mainmenu.changenick"));
        changeNickButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)   {
                mainMenu.changeNick();
            }
        });
        add(changeNickButton);
        
        setTopic.setMnemonic('T');
        setTopic.setToolTipText(I18N.get("mainmenu.settopic"));
        setTopic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)   {
                mainMenu.setTopic();
            }
        });
        add(setTopic);
        addSeparator();
        
        helpButton.setMnemonic('H');
        helpButton.setToolTipText(I18N.get("mainmenu.help"));
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)   {
                mainMenu.getHelp();
            }
        });
        add(helpButton);
        
       
        
        
    } 
    /**
     * Disables standar GUI for a button so that its just a color
     */
    private class ToolbarButton extends JButton{
        ToolbarButton(ImageIcon ic){
            super(ic);
            setBorderPainted(false); 
            setContentAreaFilled(false); 
            setFocusPainted(false); 
            setOpaque(false);
            
        }
    }
}
