package application.util.user;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.Date;

/**
 * A version of user containing only vital information for other clients.
 * (to be sent for other clients)
 */

public class SimpleUser implements Serializable {
    static final long serialVersionUID = 1L;
    String name;
    String username;
    transient Image userImg;
    long permanentID;
    Date lastSeen;
    boolean isOnline;

    SimpleUser(long permID) {
        this.permanentID = permID;
    }

    public long getID() {
        return permanentID;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date date) {
        lastSeen = date;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Image getImage() {
        return userImg;
    }

    public String getUsername() {
        return username;
    }

    public boolean usernameEquals(String username) {
        return this.username.equalsIgnoreCase(username);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleUser))
            return false;
        return permanentID == ((SimpleUser) obj).permanentID;
    }

    @Override
    public String toString() {
        return "@" + getUsername() + " " + getName();
    }
}
