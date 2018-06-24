package application.util.request;

public class ConstantConnectionRequest extends Request {
    public ConstantConnectionRequest(String username, String password) {
        super(username,password,RequestType.CONSTANT_CONNECTION,true);
    }
}
