package application.util.message;

import java.io.Serializable;
import java.util.Date;

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

    @Override
    public int hashCode() {
        return (int) (sender + target + date.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Message))
            return false;
        Message other = (Message) obj;
        return sender == other.sender && date.equals(other.date);
    }
}
