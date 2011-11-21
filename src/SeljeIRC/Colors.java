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


/**
 * @author Jon Arne Westgaard
 * @since 0.4
 * @version 1
 * Sets and returns font and color of text printed on screen
 */

public class Colors {
// Initiate colors to black:
static Color statusColor; // = "foreground=java.awt.Color[r=0,g=0,b=0]";
static Color nickColor;  // = "foreground=java.awt.Color[r=0,g=0,b=0]";
static Color channelColor;  //= "foreground=java.awt.Color[r=0,g=0,b=0]";
static Color highLightColor; // = "foreground=java.awt.Color[r=0,g=0,b=0]";
// family=Courier New foreground=java.awt.Color[r=128,g=128,b=128]
// Initiate font to Courier new:
static String font = "family=Courier New ";

public Colors () {
// Empty constructor
}
/******************************************************************************/
/* Return color and font: */

static String getStatus( String args []) {
    return font+statusColor;
}
// Return Nickcolor and font:
static String getNick( String args []) {
    return font+nickColor;
}
// Return Channelcolor and font:
static String getChannel( String args []) {
    return font+channelColor;
}
// Return hilightcolor and font:
static String getHighlight( String args []) {
    return font+highLightColor;
}

/******************************************************************************/
/* Set colors: */

static void setColors (String status, String nick, String channel, String highLight) {
    //statusColor = status;
    //nickColor = nick;
    //channelColor = channel;
    //highLightColor = highLight;
}

/******************************************************************************/
/* Set fonts: */

static void setFonts (String newFont) {
    font = newFont;
}

/******************************************************************************/

// Color-window

static void colorWindow () {
    // Set up layout for window
    final GridBagLayout colorLayout = new GridBagLayout();
    final JFrame colorBoxShit = new JFrame("Colors");
    colorBoxShit.setPreferredSize(new Dimension(400,250));
    colorBoxShit.setLayout(colorLayout);
    final GridBagConstraints gbc = new GridBagConstraints();

    // Buttons
    JButton statusColorButton = new JButton("Status Color");
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(statusColorButton, gbc);

    JButton nickColorButton = new JButton("Nick Color");
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(nickColorButton, gbc);

    JButton channelColorButton = new JButton("Channel Color");
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(channelColorButton, gbc);

    JButton highLightColorButton = new JButton("Highlight Color");
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(highLightColorButton, gbc);

    JLabel fontLabel = new JLabel("Fonts");
    gbc.insets=new Insets(2,2,2,2);
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(fontLabel, gbc);

    
    //get the local graphics environment
    GraphicsEnvironment graphicsEvn = GraphicsEnvironment.getLocalGraphicsEnvironment();
    //get all the available fonts
    String availFonts[] = graphicsEvn.getAvailableFontFamilyNames();
    JComboBox fontList = new JComboBox(availFonts);
    gbc.insets=new Insets(2,2,2,2);
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.BOTH;
    colorLayout.setConstraints(fontList, gbc);

    JButton OK = new JButton("OK");
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 1;
    gbc.gridheight = 3;
    gbc.fill = GridBagConstraints.NONE;
    colorLayout.setConstraints(OK, gbc);

    JButton cancel = new JButton("Cancel");
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
    colorBoxShit.pack();
    colorBoxShit.setVisible(true);

    // OK:
            OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent faenEgETrott) {
           colorBoxShit.setVisible(false);
           String status = "foreground=java.awt.Color[r=0,g=0,b=128]";
           String nick= "foreground=java.awt.Color[r=0,g=127,b=0]";
           String channel = "foreground=java.awt.Color[r=100,g=0,b=0]";
           String highLight = "foreground=java.awt.Color[r=0,g=46,b=0]";
           //setColors(status, nick, channel, highLight);
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
          public void actionPerformed (ActionEvent StatuzColor) {
          channelColor = JColorChooser.showDialog(colorBoxShit, font, Color.BLACK);
          }
        });

        // Choose highlight-color:
        highLightColorButton.addActionListener(new ActionListener() {
          public void actionPerformed (ActionEvent StatuzColor) {
          highLightColor = JColorChooser.showDialog(colorBoxShit, font, Color.BLACK);
          }
        });
    
}

   public static void main (String args[]) {
    Colors colorsAndFontsAndShit = new Colors ();
    }

   
}
