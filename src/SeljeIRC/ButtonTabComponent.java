package SeljeIRC;
 

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import jerklib.Channel;
 
/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and 
 * a JButton to close the tab it belongs to 
 */
public class ButtonTabComponent extends JPanel {
    
    private final tabHandler pane;
    private ConnectionHandler connection;
    int tabType;
    JLabel label;
    /**
     * Sets up button
     * @param pane
     * @param con
     * @param tt 
     */
    public ButtonTabComponent(final tabHandler pane, ConnectionHandler con, int tt) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabType = tt;
       
        connection = con;
        if (pane == null) {
            throw new NullPointerException(I18N.get("btc.null"));
        }
        this.pane = pane;
        setOpaque(false);
         
        //make JLabel read titles from JTabbedPane
            label = new JLabel() {
            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
   
        
        add(label);
        //add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        //label.setBackground(Color.black);
        
        //tab button
        JButton button = new TabButton();
        add(button);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }
    /**
     * BUTTON in tab
     */
    private class TabButton extends JButton implements ActionListener {
        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText(I18N.get("btc.closetab"));
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            setIcon(new ImageIcon("src/images/SeljeIRC-icons/8x8/delete.png"));
            
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
            //setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }
        /**
         * removes the selected tab from jtabbedpane
         * @param e 
         */
        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                
                String channel = pane.getTitleAt(i);
                //TODO check type!
                
                if( tabType == SingleTab.CHANNEL)   {
                    Iterator<Channel> it = connection.getCurrentSession().getChannels().iterator();
                    while(it.hasNext())
                        System.out.println(it.next().getName());
                    connection.disconnectFromChannel(channel);
                }
               
                pane.remove(i);
                //pane.setSelectedIndex(i-1);
                
            }
        }
    }
    /**
     * mouselistener for hover effects, graphic to be implemented
     */
    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }
        /**
         * what state to return to after hover
         */
        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
        
    };
}