package application.util.user;

import java.io.Serializable;
import java.util.Set;

public class Group implements Serializable {
    static final long serialVersionUID = 1L;
    private Info info;

    public Group(long permID, String name, Set<Long> members) {
        info = new Info(permID, true);
        info.members = members;
        info.name = name;
    }

    public Info getInfo() {
        return info;
    }

    public long getID() {
        return info.permanentID;
    }

    public Set<Long> getMembers(){
        return info.members;
    }
}
