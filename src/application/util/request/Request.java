package application.util.request;

import java.io.Serializable;

public abstract class Request implements Serializable {
    static final long serialVersionUID = 1L;
    public final String username;
    public final String password;
    public final RequestType type;

    Request(String username, String password, RequestType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }
}
