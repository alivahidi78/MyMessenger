package application.util.user;

import java.io.Serializable;
import java.util.Date;

/**
 * A version of user containing only vital information for other clients.
 * (to be sent for other clients)
 */

public class SimpleUser implements Serializable {
    static final long serialVersionUID = 1L;
    private String name;
    private String username;
    private long permanentID;
    private Date lastSeen;
    private transient boolean isOnline;

    SimpleUser(String name, String username) {
        this.name = name;
        this.username = username;
        //TODO create permID
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline() {
        isOnline = true;
    }

    public void setOffline() {
        isOnline = false;
        lastSeen = new Date();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean usernameEquals(String username) {
        return this.username.equalsIgnoreCase(username);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleUser))
            return false;
        return permanentID == ((SimpleUser) obj).permanentID;
    }

    protected SimpleUser get() {
        return this;
    }
}
