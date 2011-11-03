/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SeljeIRC;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 *
 * @author wbserver
 */
public class MainMenu extends JMenuBar {
    private JMenu file = new JMenu("File");
    private JMenu edit = new JMenu("Edit");
    private JMenu help = new JMenu("Help");
    
    public MainMenu(){
        super();
        
        add(file);
        add(edit);
        add(help);
    }
    
}
