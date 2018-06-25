package application.util.message.info;

import java.util.Date;
import java.util.Set;

public class UserStatusInfoMessage extends ServerInfoMessage {
    public final boolean isOnline;
    public final long id;
    public final Date lastSeen;

    public UserStatusInfoMessage(Set<Long> targets, Date date, boolean isOnline, long id, Date lastSeen) {
        super(InfoMessageType.UPDATE_USER_STATUS, targets, date);
        this.id = id;
        this.isOnline = isOnline;
        this.lastSeen = lastSeen;
    }
}
