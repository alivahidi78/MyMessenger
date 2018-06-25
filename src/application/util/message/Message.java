package application.util.message;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public abstract class Message implements Serializable {
    static final long serialVersionUID = 1L;
    public final long sender;
    public final Set<Long> targets;
    public final Date date;
    public final MessageType type;

    public Message(MessageType type, long sender, Set<Long> targets, Date date) {
        this.sender = sender;
        this.targets = targets;
        this.date = date;
        this.type = type;
    }
}
