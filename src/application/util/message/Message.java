package application.util.message;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public abstract class Message implements Serializable {
    static final long serialVersionUID = 1L;
    public final long sender;
    public final long target;
    public final long group;
    public final Date date;
    public final MessageType type;
    public final boolean isFromServer;
    public final boolean isFromGroup;
    public Message(MessageType type, long sender, long group, long target, Date date) {
        this.sender = sender;
        this.target = target;
        this.date = date;
        this.type = type;
        this.group = group;
        isFromGroup = group >= 0;
        isFromServer = sender < 0;
    }
}
