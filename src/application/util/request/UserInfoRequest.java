package application.util.request;

public class UserInfoRequest extends Request {
    public UserInfoRequest(String username, String password) {
        super(username, password, RequestType.GET_USER_INFO, true);
    }
}
