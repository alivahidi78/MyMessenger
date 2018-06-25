package application.util.request;

public class MessagingConnectionRequest extends Request {
    public MessagingConnectionRequest(String username, String password) {
        super(username,password,RequestType.MESSAGING_CONNECTION,true);
    }
}
