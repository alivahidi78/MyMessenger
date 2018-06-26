package application.util.user;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * A version of user containing only vital information for other clients.
 * (to be sent for other clients)
 */

public class Info implements Serializable {
    static final long serialVersionUID = 1L;
    String name;
    String username;
    transient Image userImg;
    long permanentID;
    Date lastSeen;
    boolean isOnline;
    boolean isGroup;
    Set<Long> members;
    Info(long permID) {
        this.permanentID = permID;
    }

    Info(long permID, boolean isGroup) {
        this(permID);
        this.isGroup = isGroup;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public Set<Long> getMembers() {
        return members;
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
        if (!(obj instanceof Info))
            return false;
        return permanentID == ((Info) obj).permanentID;
    }

    @Override
    public String toString() {
        return "@" + getUsername() + " " + getName();
    }
}
