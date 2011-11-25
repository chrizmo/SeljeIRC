package SeljeIRC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.text.StyleConstants;


/**
 * Sets color, font and fontsize
 * @author Jon Arne Westgaard
 * @since 0.4
 * @version 1
 * 
 */

public class Colors {
public static Color statusColor;
public static Color nickColor;
public static Color channelColor;
public static Color highLightColor;
public static String font;
public static int fontSize;

public Colors () {
/**
 * Sets the color to black
 * @author Jon Arne Westgaard
 */
statusColor = new Color(153, 153, 153);
nickColor = new Color(2, 2, 73);
channelColor = new Color(54, 1, 47);
highLightColor = new Color(0, 0, 0);
font = "Serif";
fontSize = 12;
}

/**
 * Creates the window for choosing colors and font
 * @author Jon Arne Westgaard
 * @since 0.4
 * @version 0.8
 */
static void colorWindow () {
    // Set up layout for window
    final GridBagLayout colorLayout = new GridBagLayout();
    final JFrame colorBoxShit = new JFrame(I18N.get("colors.andfonts"));
    colorBoxShit.setPreferredSize(new Dimension(400,250));
    colorBoxShit.setLayout(colorLayout);
    final GridBagConstraints gbc = new GridBagConstraints();

    // Statuscolor-button:
    JButton statusColorButton = new JButton(I18N.get("colors.status"));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(statusColorButton, gbc);

    // Nickcolor-button:
    JButton nickColorButton = new JButton(I18N.get("colors.nickcolor"));
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(nickColorButton, gbc);

    // Channelcolor-button:
    JButton channelColorButton = new JButton(I18N.get("colors.channelcolor"));
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(channelColorButton, gbc);

    // Highlightcolor-button:
    JButton highLightColorButton = new JButton(I18N.get("colors.highlight"));
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(highLightColorButton, gbc);

    // Fontlabel:
    JLabel fontLabel = new JLabel(I18N.get("colors.fonts"));
    gbc.insets=new Insets(2,2,2,2);
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(fontLabel, gbc);
    
    // Get the local graphics environment
    GraphicsEnvironment graphicsEvn = GraphicsEnvironment.getLocalGraphicsEnvironment();

    // Get all the available fonts at this computer:
    final String availFonts[] = graphicsEvn.getAvailableFontFamilyNames();
    // Add all the available fonts to a JCombobox:
    final JComboBox fontList = new JComboBox(availFonts);
    
    gbc.insets=new Insets(2,2,2,2);
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(fontList, gbc);

    // Fontsize-label:
    final JLabel fontSizeLabel = new JLabel(I18N.get("colors.fontsize"));
    gbc.insets = new Insets(2,2,2,2);
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(fontSizeLabel, gbc);

    // Simply use a string with the fontsizes the user can choose:
    final String fontZize[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9","10",
    "11", "12", "13", "14", "15", "16","17", "18", "19", "20"};
    final JComboBox setFontSize = new JComboBox(fontZize);
    setFontSize.setSelectedIndex(11);
    gbc.gridx = 1;
    gbc.gridy = 3;
    colorLayout.setConstraints(setFontSize, gbc);
    
    
    // OK-button:
    JButton OK = new JButton(I18N.get("colors.OK"));
    gbc.insets = new Insets(4,4,4,4);
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 1;
    gbc.gridheight = 3;
    gbc.fill = GridBagConstraints.NONE;
    colorLayout.setConstraints(OK, gbc);

    // Cancel-button:
    JButton cancel = new JButton(I18N.get("colors.cancel"));
    gbc.gridx = 1;
    gbc.gridy = 6;
    gbc.gridwidth = 1;
    gbc.gridheight = 2;
    gbc.fill = GridBagConstraints.NONE;
    colorLayout.setConstraints(cancel, gbc);

    // Add 'em:
    colorBoxShit.add(OK);
    colorBoxShit.add(statusColorButton);
    colorBoxShit.add(nickColorButton);
    colorBoxShit.add(channelColorButton);
    colorBoxShit.add(highLightColorButton);
    colorBoxShit.add(fontLabel);
    colorBoxShit.add(cancel);
    colorBoxShit.add(fontList);
    colorBoxShit.add(fontSizeLabel);
    colorBoxShit.add(setFontSize);
    colorBoxShit.pack();
    colorBoxShit.setVisible(true);

    // OK-actionlistener:
            OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent faenEgETrott) {
           colorBoxShit.setVisible(false);
            }
        });

     // Cancel-actionlistener:
           cancel.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent BareGlemDet) {
           colorBoxShit.setVisible(false);
            }
        });

        // Choose Status-color:
        statusColorButton.addActionListener(new ActionListener() {
          public void actionPerformed (ActionEvent StatuzColor) {
          statusColor = JColorChooser.showDialog(colorBoxShit, font, Color.BLACK);
          }
        });

        // Choose nick-color:
        nickColorButton.addActionListener(new ActionListener() {
          public void actionPerformed (ActionEvent nikk) {
          nickColor = JColorChooser.showDialog(colorBoxShit, font, Color.BLACK);
          }
        });

        // Choose channel-color:
        channelColorButton.addActionListener(new ActionListener() {
          public void actionPerformed (ActionEvent chColor) {
          channelColor = JColorChooser.showDialog(colorBoxShit, font, Color.BLACK);
          }
        });

        // Choose highlight-color:
        highLightColorButton.addActionListener(new ActionListener() {
          public void actionPerformed (ActionEvent hilightColor) {
          highLightColor = JColorChooser.showDialog(colorBoxShit, font, Color.BLACK);
          }
        });

    // Choose font:
    fontList.addActionListener(new ActionListener() {
          public void actionPerformed (ActionEvent setSelectedFont) {
          int fnr = fontList.getSelectedIndex();
          font = availFonts[fnr];
          }
        });

    // Choose fontsize:
    setFontSize.addActionListener(new ActionListener() {
          public void actionPerformed (ActionEvent setTheFontSize) {
          fontSize = setFontSize.getSelectedIndex();
          
          }
        });
}
}
