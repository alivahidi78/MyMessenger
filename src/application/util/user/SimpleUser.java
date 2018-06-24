package application.util.user;

import javafx.scene.image.Image;

import java.io.Serializable;

/**
 * A version of user containing only vital information for other clients.
 * (to be sent for other clients)
 */

public class SimpleUser implements Serializable {
    static final long serialVersionUID = 1L;
    String name;
    String username;
    Image userImg;
    private long permanentID;

    SimpleUser(long permID) {
        this.permanentID = permID;
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
