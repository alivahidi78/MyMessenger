package application.util.user;

public class User extends SimpleUser {
    private String password;
    //TODO add chats, associates
    public User(String name, String username, String password) {
        super(name, username);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean passwordEquals(String password) {
        return this.password.equals(password);
    }

    public SimpleUser getSimpleUser() {
        //TODO
        return super.get();
    }
}
