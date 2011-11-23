package SeljeIRC;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Used for rendering the nick list for every channel. Each list contains {@link UserListModel.User] objects.
 * @author larserik
 * @version 0.1
 * @since 0.1
 */
public class UserListRenderer extends JLabel implements ListCellRenderer  {
    
    /**
     * Gets the object int the list which are going to be rendered
     * @param list The actual list to be rendered
     * @param o The specific object within the list
     * @param index Index of Object o int he list
     * @param isSelected Is the object at this index selected?
     * @param hasFocus Does the object at this index have focus?
     * @return a JLabel with the rendered text
     */
    @Override
    public JLabel getListCellRendererComponent(JList list, Object o, int index, boolean isSelected, boolean hasFocus) {
        setOpaque(true);                                                    // Makes sure the component is properly painted
        if (index >= 0)   {                                                 // The list is not empty
            if (isSelected)   {                                             
                setBackground(list.getSelectionBackground());               // Paint selection background color
                setForeground(list.getSelectionForeground());               // Paint selection foreground color
            }
            else   {                                                        // Not selected
                setBackground(list.getBackground());                        // Paint normal background color
                setForeground(list.getForeground());                        // Paint normal foreground color
            }
            String nick = ((UserListModel.User)o).toString();               // Gets the nickname from the User object
            setText(nick);                                                  // Put it in the label
        }
       return this;                                                         
    }  
}
