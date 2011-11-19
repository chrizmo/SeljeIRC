package SeljeIRC;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author Jon Arne Westgaard
 * @since 0.4
 * @version 1
 * Sets and returns font and color of text printed on screen
 */

public class Colors {
// Initiate colors to black:
static String statusColor = "java.awt.Color[r=0,g=0,b=0]";
static String nickColor = "java.awt.Color[r=0,g=0,b=0]";
static String channelColor = "java.awt.Color[r=0,g=0,b=0]";
static String highlightColor = "java.awt.Color[r=0,g=0,b=0]";

// Initiate font to Courier new:
static String statusFont = "family=Courier New";
static String nickFont = "family=Courier New";
static String channelFont = "family=Courier New";
static String highlightFont = "family=Courier New";

public Colors () {
// Empty constructor
}
/******************************************************************************/
/* Return color and font: */

static String getStatus( String args []) {
    return statusColor+statusFont;
}
// Return Nickcolor and font:
static String getNick( String args []) {
    return nickColor+nickFont;
}
// Return Channelcolor and font:
static String getChannel( String args []) {
    return channelColor+channelFont;
}
// Return hilightcolor and font:
static String getHighlight( String args []) {
    return highlightColor+highlightFont;
}

/******************************************************************************/
/* Set colors: */

static void setColors (String status, String nick, String channel, String highLight) {
    statusColor = status;
    nickColor = nick;
    channelColor = channel;
    highlightColor = highLight;
}

/******************************************************************************/
/* Set fonts: */

static void setFonts (String status, String nick, String channel, String highLight) {
    statusFont = status;
    nickFont = nick;
    channelFont = channel;
    highlightFont = highLight;
}

/******************************************************************************/

// Color-window

static void colorWindow () {
    // Set up layout for window
    GridBagLayout colorLayout = new GridBagLayout();
    final JFrame colorBoxShit = new JFrame("Colors");
    colorBoxShit.setPreferredSize(new Dimension(500,500));
    colorBoxShit.setLayout(colorLayout);
    // Buttons
    JButton statusColorButton = new JButton("Status Color");
    JButton nickColorButton = new JButton("Nick Color");
    JButton channelColorButton = new JButton("Channel Color");
    JButton highLightColorButton = new JButton("Highlight Color");
    JButton statusFontButton = new JButton("Status font");
    JButton nickFontButton = new JButton("Nick font");
    JButton channelFontButton = new JButton("Channel font");
    JButton highLightFontButton = new JButton("Highlight font");
    JButton OK = new JButton("OK");
    JButton cancel = new JButton("Cancel");

    // Add 'em:
    colorBoxShit.add(OK);
    colorBoxShit.add(statusColorButton);
    colorBoxShit.add(nickColorButton);
    colorBoxShit.add(channelColorButton);
    colorBoxShit.add(highLightColorButton);
    colorBoxShit.add(statusFontButton);
    colorBoxShit.add(nickFontButton);
    colorBoxShit.add(channelFontButton);
    colorBoxShit.add(highLightFontButton);
    colorBoxShit.pack();
    colorBoxShit.setVisible(true);

            OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent faenEgETrøtt) {
           colorBoxShit.setVisible(false);
            }
        });
    String status = "java.awt.Color[r=0,g=0,b=128]";
    String nick= "java.awt.Color[r=0,g=127,b=0]";
    String channel = "java.awt.Color[r=100,g=0,b=0]";
    String highlight = "java.awt.Color[r=0,g=46,b=0]";
    setColors(status, nick, channel, highlight);
    System.out.println(status+ nick+ channel+ highlight);
}

   public static void main (String args[]) {
    Colors colorsAndFontsAndShit = new Colors ();
    }
}
