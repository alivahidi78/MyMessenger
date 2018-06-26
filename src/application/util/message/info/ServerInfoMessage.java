package application.util.message.info;

import application.util.message.Message;
import application.util.message.MessageType;

import java.util.Date;
import java.util.Set;

public abstract class ServerInfoMessage extends Message {
    public final InfoMessageType infoType;

    ServerInfoMessage(InfoMessageType infoType, long target, Date date) {
        super(MessageType.SERVER_INFO, -2,-2, target, date);
        this.infoType = infoType;
    }

}
