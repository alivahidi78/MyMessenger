package application.util.user;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String name;
    private long permanentID;
    private Date lastSeen;
    private transient boolean isOnline;
    //TODO add chats, associates
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
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
    public void setOffline(){
        isOnline = false;
        lastSeen = new Date();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean passwordEquals(String password) {
        return this.password.equals(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimpleUser getSimpleUser() {
        //TODO
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;
        return permanentID == ((User) obj).permanentID;
    }
}
