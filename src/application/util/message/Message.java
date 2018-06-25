package application.util.message;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

public class Message implements Serializable {
    static final long serialVersionUID = 1L;
    public final long sender;
    public final LinkedList<Long> targets;
    public final Date date;
    public final MessageType type;

    public Message(MessageType type, long sender, LinkedList<Long> targets, Date date) {
        this.sender = sender;
        this.targets = targets;
        this.date = date;
        this.type = type;
    }
}
