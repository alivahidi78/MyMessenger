package application.util.request;

public class SearchConnectionRequest extends Request {
    public SearchConnectionRequest(String username, String password) {
        super(username, password, RequestType.SEARCH_CONNECTION,true);
    }
}
