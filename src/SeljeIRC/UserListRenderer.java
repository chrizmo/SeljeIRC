/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author larserik
 */
public class UserListRenderer extends JLabel implements ListCellRenderer  {
    
    @Override
    public JLabel getListCellRendererComponent(JList list, Object o, int index, boolean isSelected, boolean hasFocus) {
        setOpaque(true);
        if (index >= 0)   {
            if (isSelected)   {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else   {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            String nick = ((UserListModel.User)o).toString();
            setText(nick);
        }
       
       
       return this; 
    }
    
}
