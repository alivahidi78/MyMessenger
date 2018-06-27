package application.util.message;

import java.util.Date;

public class FileMessage extends Message {
    public FileMessage(MessageType type, long sender, long group, long target, Date date) {
        super(type, sender, group, target, date);
    }
}
