package application.util.user;

import application.util.message.Message;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class User implements Serializable {
    static final long serialVersionUID = 1L;
    Set<Long> associates;
    private String password;
    private SimpleUser info;
    private Date lastSeen;
    private transient boolean isOnline;
    private Map<Long, List<Message>> chats;

    //TODO add chats, associates
    public User(long permID, String name, String username, String password) {
        info = new SimpleUser(permID);
        info.name = name;
        info.username = username;
        this.password = password;
    }

    public Image getImage() {
        return info.userImg;
    }

    public void setImage(Image image) {
        info.userImg = image;
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
        return info.username;
    }

    public void setUsername(String username) {
        info.username = username;
    }

    public boolean usernameEquals(String username) {
        return info.usernameEquals(username);
    }

    public String getName() {
        return info.name;
    }

    public void setName(String name) {
        info.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;
        return info.equals(((User) obj).info);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean passwordEquals(String password) {
        return this.password.equals(password);
    }

    public SimpleUser getSimpleUser() {
        return this.info;
    }
}
