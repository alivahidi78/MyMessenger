package application.util.request;

public class UserInfoConnectionRequest extends Request {
    public UserInfoConnectionRequest(String username, String password) {
        super(username, password, RequestType.USER_INFO_CONNECTION, true);
    }
}
