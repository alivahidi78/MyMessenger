package application.util.message.info;

import java.util.Date;
import java.util.Set;

public class GroupAdditionInfoMessage extends ServerInfoMessage {
    public final long groupID;

    public GroupAdditionInfoMessage(long target, long groupID, Date date) {
        super(InfoMessageType.ADDITION_TO_GROUP, target, date);
        this.groupID = groupID;
    }
}
