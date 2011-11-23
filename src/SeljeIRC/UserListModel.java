package SeljeIRC;

import javax.swing.DefaultListModel;

/**
 * Model for the Channel's user list
 * @author Lars Erik Pedersen
 * @version 0.1
 * @since 0.1
 */
public class UserListModel extends DefaultListModel {
    
    /**
     * Adds a regular user to the user list
     * @author Lars Erik Pedersen
     * @since 0.1
     * @param s Nick which shall be added to the user list
     */
    public void addUserToList(String s)   {
        User newUser = new User(s, false, false);
        if (size() == 0)   {                                // Empty list
            super.addElement(newUser);                      // Add the user first
        }
        else   {
            insert(newUser);                                // Insert user
        }
        fireContentsChanged(this, 0, size());               // Redraw list
    }
    
    /**
     * Removes a user from the list
     * @author Lars Erik Pedersen
     * @since 0.1
     * @param s Nick that shall be removed
     */
    public void removeUser(String s)   {
        User tmp = getUser(s);                      // Gets the user object with given nick
        super.removeElement(tmp);                   // Remove it
        fireContentsChanged(this, 0, size());       // Redraw list
    }
    
    /**
     * Ops or deops a user, and re-arrange the user list
     * @author Lars Erik Pedersen
     * @since 0.1
     * @param s Nick to be oped/deoped
     * @param o op or deop
     */
    public void op(String s, boolean o)   {                 // Will allways be called after the complete userlist is initialized
        User tmp = getUser(s);                              // Fetch user with nickname s
        if (tmp != null)   {                                // User is in list
            if (tmp.voice && o)   {                             // Sorting goes crazy if the user is both op and voice.....
                tmp.setVoice(false);                            // DIRTY, Temporarly remove voice, to avoid sorting error
                tmp.setOp(o);                                   // Set the op mode
                removeElement(tmp);                             // Delete it from list
                insert(tmp);                                    // Insert it on its new place
                tmp.setVoice(true);                             // DIRTY, Give him the voice back...
            }
            else   {                                        // Only op set, no problem :)
                tmp.setOp(o);                               // Set the op bit
                removeElement(tmp);                         // Remove user from list
                insert(tmp);                                // Put him back in the right position
            }
            fireContentsChanged(this, 0, size());           // Redraw table
        }
        else System.out.println("User " + s + " not in list");  // For debugging, shall never occur!
        
    }
    
    /**
     * Voices or devoices a user, and re-arrange the user list.
     * @author Lars Erik Pedersen
     * @since 0.1
     * @param s Nick to be voiced/devoiced
     * @param v voice or devoice
     */
    public void voice(String s, boolean v)   {              // See above comments
        User tmp = getUser(s);  
        if (tmp != null)   {
            if (tmp.op)   {                                 // Shall not rearrange list, if a op gets voice
                tmp.setVoice(v);
            }
            else   {
                tmp.setVoice(v);                            // Regular user gets or loses voice
                removeElement(tmp);                         // Remove user
                insert(tmp);                                // Put him back in the right position
                fireContentsChanged(this, 0, size());       // Redraw
            }
        }
        else System.out.println("User " + s + " not in list");  // For debugging, shall never occur!
    }
    
    /**
     * Inserts a user in the user list, and sorts it ascending
     * @author Lars Erik Pedersen
     * @since 0.1
     * @param n User to insert
     */
    private void insert(User n)   {
        int i;
        for (i = 0; i < size(); i++)   {                // Iterate through all users
            User userInList = (User)elementAt(i);       // Pick user
            if (userInList.compareTo(n) <= 0)   {       // Continue until current user is "bigger" than the new user
                continue;
            }
            insertElementAt(n, i);                      // Insert the new user where the current user was
            return;                                     // No need for more searching after user is added
        }
        super.addElement(n);                            // New user was the "biggest" insert at the bottom
    }
    
    /**
     * Get the user object from a given nickname
     * @param s Nickname to be searched for
     * @return The User object with nickname s, or null if not found
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    private User getUser(String s)   {
        User tmp = new User(s, false, false);       // Create a User object with given nick, to fetch from list
        int i = indexOf(tmp);                       // Gets the index of that user
        if (i != -1 ) return (User)elementAt(i);    // The user is in the list
        else return null;                           // The user is not in the list
    }
    
    /**
     * Changes the nick of a user in the list
     * @param oldNick The user's old nick
     * @param newNick The user's new nick
     * @author Lars Erik Pedersen
     * @since 0.1
     */
    public void changeNick(String oldNick, String newNick) {
        User tmp = getUser(oldNick);                // Fetch user object
        removeUser(oldNick);                        // Remove him
        tmp.nick = newNick;                         // Change nick
        insert(tmp);                                // Put him back at right position
        if (tmp.op) op(newNick, true);              // If he is op, make sure he still is
        else if (tmp.voice) voice(newNick, true);   // Same for voice
        fireContentsChanged(this, 0, size());       // Redraw
    }

    /**
     * Checks wether a user is channel operator or not
     * @param user Nickname of the user you want to check
     * @return True if user is operator, otherwise false
     * @author Lars Erik Pederseb
     * @since 0.1
     */
    public boolean isOp(String user) {
        if (getUser(user).op) return true;
        else return false;
    }

    /**
     * Checks wether a user is voiced or not
     * @param user Nickname of the user you want to check
     * @return True if user is voiced, otherwise false
     * @author Lars Erik Pederseb
     * @since 0.1
     */
    public boolean isVoice(String user) {
        if (getUser(user).voice) return true;
        else return false;
    }

    /**
     * Inner class. Defines a User within the channel
     * @author Lars Erik Pedersen
     * @since 0.1
     * @version 0.1
     */
    class User implements Comparable<User>   {
        String nick;
        boolean op;
        boolean voice;
        
        /**
         * Constructor, creates a user with nickname and channel modes
         * @author Lars Erik Pedersen
         * @since 0.1
         * @param n Nickname
         * @param o Operator
         * @param v Voiced
         */
        public User(String n, boolean o, boolean v)   {
            nick = n;
            op = o;
            voice = v;
        }
        
        /**
         * Sets operator mode
         * @author Lars Erik Pedersen
         * @since 0.1
         * @param o True = Op, False = deop
         */
        public void setOp(boolean o)   {
            op = o;
        }
        
        /**
         * Set voice mode
         * @author Lars Erik Pedersen
         * @since 0.1
         * @param v True = Voice, Flase = devoice
         */
        public void setVoice(boolean v)   {
            voice = v;
        }
        
        /**
         * Set the users nick
         * @author Lars Erik Pedersen
         * @since 0.1
         * @param n 
         */
        public void setNick(String n)   {
            nick = n;
        }
        
        /**
         * Compares a User to another User. Used for sorting in the channels user list. Op shall be listed alphabeticly on the top of the list,
         * followed by voiced users, then a alphabetic list of normal users.
         * @author Lars Erik Pedersen
         * @param u The User to compare this User object with
         * @return A numeric value. Negative means that this object is "smaller" the the parameter. Positive means that this object is "larger" than the parameter
         * @since 0.1
         */
        @Override
        public int compareTo(User u) {
            if (op && !u.op)
                return -1;
            if (!op && u.op)
                return 1;
            if (voice && !u.voice)
                return -1;
            if (!voice && u.voice)
                return 1;
            return nick.compareToIgnoreCase(u.nick);
        }
        
        /**
         * Checks if to Users are equal. Uses the nickname, because that will be uniqe for every User.
         * @author Lars Erik Pedersen
         * @param o Object to be compared with
         * @return True if the Users are equal, false if they're not.
         * @since 0.1
         */
        @Override
        public boolean equals(Object o)   {
            if (o instanceof User)   {                  // Compared with another User object
                return nick.equals(((User)o).nick);
            }
            else if (o instanceof String) {             // Compared with a String
                return nick.equals((String)o);
            }  
            else return false;                          // Compared with an illegal object
        }
        
        /**
         * Returns the literal string of the User object
         * @author Lars Erik Pedersen
         * @since 0.1
         * @return Username with op or voice prefixed
         */
        @Override
        public String toString()   {
            StringBuilder s = new StringBuilder();
            s.append( (op) ? "@" : "" );
            s.append( (voice && !op) ? "+" : "" );      // Dont show both a @ and a +
            s.append(nick);
            return s.toString();
        }
    }
}