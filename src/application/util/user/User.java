package application.util.user;

import application.util.message.Message;
import javafx.scene.image.Image;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
    static final long serialVersionUID = 1L;
    private Set<Long> associates = new LinkedHashSet<>();
    private String password;
    private SimpleUser info;
    private Date lastSeen;
    private transient ObjectOutputStream outputStream;
    private transient boolean isOnline;
    private Map<Long, List<Message>> chats = new LinkedHashMap<>();
    //TODO add chats, associates
    public User(long permID, String name, String username, String password) {
        info = new SimpleUser(permID);
        info.name = name;
        info.username = username;
        this.password = password;
    }

    public Set<Long> getAssociates() {
        return associates;
    }

    public void addAssociate(long id) {
        this.associates.add(id);
    }

    public void addMessage(long id, Message message) {
        List<Message> messageList = this.chats.get(id);
        if (messageList == null) {
            messageList = new LinkedList<>();
            messageList.add(message);
            chats.put(id, messageList);
        } else {
            chats.get(id).add(message);
        }
    }

    public Map<Long, List<Message>> getChats() {
        return chats;
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
        outputStream = null;
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

    public long getID() {
        return info.permanentID;
    }

    public ObjectOutputStream getOutput() {
        return outputStream;
    }

    public void setOutput(ObjectOutputStream out) {
        this.outputStream = out;
    }
}
