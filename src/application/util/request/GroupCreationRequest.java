package application.util.request;

import java.util.Set;

public class GroupCreationRequest extends Request {
    public final String name;
    public final Set<Long> members;

    public GroupCreationRequest(String username, String password, String name, Set<Long> members) {
        super(username, password, RequestType.GROUP_CREATION, false);
        this.name = name;
        this.members = Set.of(members.toArray(new Long[0]));
    }
}
