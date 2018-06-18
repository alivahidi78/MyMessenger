package application.util.request;

public class SignUpRequest extends Request {
    public final String name;
    public SignUpRequest(String name, String username, String password) {
        super(username,password,RequestType.SIGN_UP);
        this.name = name;
    }
}
