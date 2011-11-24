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
 * @author Jon Arne Westgaard
 * @since 0.4
 * @version 1
 * Sets and returns font and color of text printed on screen
 */

public class Colors {
// Initiate colors to black, and font and size:
public static Color statusColor = new Color(0, 0, 0);
public static Color nickColor = new Color(0, 0, 0);
public static Color channelColor = new Color(0, 0, 0);
public static Color highLightColor = new Color(0, 0, 0);
public static String font = "Serif";
public static int fontSize = 12;

public Colors () {
// Empty constructor
}


// Color-window:
static void colorWindow () {
    // Set up layout for window
    final GridBagLayout colorLayout = new GridBagLayout();
    final JFrame colorBoxShit = new JFrame(I18N.get("colors.andfonts"));
    colorBoxShit.setPreferredSize(new Dimension(400,250));
    colorBoxShit.setLayout(colorLayout);
    final GridBagConstraints gbc = new GridBagConstraints();

    // Buttons
    JButton statusColorButton = new JButton(I18N.get("colors.status"));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(statusColorButton, gbc);

    JButton nickColorButton = new JButton(I18N.get("colors.nickcolor"));
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(nickColorButton, gbc);

    JButton channelColorButton = new JButton(I18N.get("colors.channelcolor"));
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(channelColorButton, gbc);

    JButton highLightColorButton = new JButton(I18N.get("colors.highlight"));
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(highLightColorButton, gbc);

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

    // Get all the available fonts
    final String availFonts[] = graphicsEvn.getAvailableFontFamilyNames();
    final JComboBox fontList = new JComboBox(availFonts);
    
    gbc.insets=new Insets(2,2,2,2);
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(fontList, gbc);

    final JLabel fontSizeLabel = new JLabel(I18N.get("colors.fontsize"));
    gbc.insets = new Insets(2,2,2,2);
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(fontSizeLabel, gbc);

    final String fontZize[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9",
    "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    final JComboBox setFontSize = new JComboBox(fontZize);
    setFontSize.setSelectedIndex(11);
    gbc.gridx = 1;
    gbc.gridy = 3;
    colorLayout.setConstraints(setFontSize, gbc);
    
    

    JButton OK = new JButton(I18N.get("colors.OK"));
    gbc.insets = new Insets(4,4,4,4);
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 1;
    gbc.gridheight = 3;
    gbc.fill = GridBagConstraints.NONE;
    colorLayout.setConstraints(OK, gbc);

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

    // OK:
            OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent faenEgETrott) {
           colorBoxShit.setVisible(false);
            }
        });

     // Cancel:
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
          int snr = setFontSize.getSelectedIndex();
          fontSize = snr;
          }
        });
}

   public static void main (String args[]) {
    Colors colorsAndFontsAndShit = new Colors ();
    }
}
