package application.util.message;

import application.util.user.User;

import java.io.Serializable;
import java.util.LinkedList;

public class Message implements Serializable {
    static final long serialVersionUID = 1L;
    private LinkedList<User> targets;
}
