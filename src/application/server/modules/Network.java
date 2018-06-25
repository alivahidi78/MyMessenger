package application.server.modules;

import application.util.answer.*;
import application.util.message.Message;
import application.util.request.*;
import application.util.user.SimpleUser;
import application.util.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Handles the connection between the clients and the server.
 */
public class Network {
    private static ServerSocket serverSocket;
    private static Database db = Database.getInstance();
    private static ConcurrentLinkedDeque<Connection> onlineUsers = new ConcurrentLinkedDeque<>();

    private static void handleCCRequest(Request request, ObjectInputStream in, ObjectOutputStream out) throws IOException {
        ConstantConnectionRequest ccRequest = (ConstantConnectionRequest) request;
        String username = ccRequest.username;
        String password = ccRequest.password;
        Optional<User> user = db.findUserByUsername(username);
        if (!user.isPresent() || !user.get().passwordEquals(password)) {
            out.writeObject(new SignInDeniedAnswer());
        } else {
            Connection connection = new Connection(in, out, user.get());
            out.writeObject(new SignInAcceptedAnswer(user.get()));
            onlineUsers.add(connection);
            createConstantConnection(connection);
        }
    }

    private static void handleSignUpRequest(Request request, ObjectInputStream in, ObjectOutputStream out) throws IOException {
        SignUpRequest suRequest = (SignUpRequest) request;
        String name = suRequest.name;
        String username = suRequest.username.toLowerCase();
        String password = suRequest.password;
        Optional<User> duplicate = db.findUserByUsername(username);
        if (duplicate.isPresent()) {
            out.writeObject(new SignUpDeniedAnswer());
        } else {
            out.writeObject(new SignUpAcceptedAnswer());
            db.createNewUser(name, username, password);
        }
    }

    private static void handleSearchRequest(Request request, ObjectInputStream in, ObjectOutputStream out) throws IOException {
        SearchConnectionRequest scRequest = (SearchConnectionRequest) request;
        Optional<User> user = db.findUserByUsername(scRequest.username);
        if (user.isPresent()) {
            Connection connection = new Connection(in, out, user.get());
            out.writeObject(new ConnectedAnswer());
            createSearchConnection(connection);
        }
    }

    private static void handleUserInfoRequest(Request request, ObjectInputStream in, ObjectOutputStream out) throws IOException {
        UserInfoRequest uiRequest = (UserInfoRequest) request;
        out.writeObject(new ConnectedAnswer());
        out.flush();
        createUserInfoConnection(in, out);
    }


    public static void processRequests(ServerSocket serverSocket) throws IOException {
        Network.serverSocket = serverSocket;
        while (true) {
            Socket socket = Network.serverSocket.accept();
            new Thread(() -> {
                try {
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    Request request = (Request) in.readObject();
                    switch (request.type) {
                        case CONSTANT_CONNECTION:
                            handleCCRequest(request, in, out);
                            break;
                        case SIGN_UP:
                            handleSignUpRequest(request, in, out);
                            break;
                        case UPLOAD_FILE:
                            //TODO
                            break;
                        case CHANGE_USER_INFO:
                            //TODO
                            break;
                        case SEARCH_CONNECTION:
                            handleSearchRequest(request, in, out);
                            break;
                        case GET_USER_INFO:
                            handleUserInfoRequest(request, in, out);
                            break;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void createConstantConnection(Connection connection) {
        ObjectInputStream in = connection.getInput();
        connection.getUser().setOutput(connection.getOutput());
        Thread thread = new Thread(() -> {
            boolean connected = true;
            while (connected) {
                try {
                    Message message = (Message) in.readObject();
                    db.processMessage(message);
                    //try-catch todo
                    Network.processMessage(message);
                } catch (IOException e) {
                    connected = false;
                    connection.disconnect();
                    onlineUsers.remove(connection);
                    //disconnected
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //TODO
        });
        thread.setDaemon(true);
        thread.start();
    }

    private static void createSearchConnection(Connection connection) {
        ObjectInputStream in = connection.getInput();
        ObjectOutputStream out = connection.getOutput();
        Thread thread = new Thread(() -> {
            boolean connected = true;
            while (connected) {
                try {
                    String search = (String) in.readObject();
                    List<SimpleUser> foundUsers = db.searchFor(search);
                    foundUsers.remove(connection.getUser().getSimpleUser());
                    out.writeObject(foundUsers);
                    out.flush();
                } catch (IOException e) {
                    connected = false;
                    connection.disconnect();
                } catch (ClassNotFoundException e) {
                    connected = false;
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private static void createUserInfoConnection(ObjectInputStream in, ObjectOutputStream out) {
        Thread thread = new Thread(() -> {
            boolean connected = true;
            while (connected) {
                try {
                    Long id = (Long) in.readObject();
                    Optional<User> user = db.findUserByID(id);
                    if (user.isPresent())
                        out.writeObject(user.get().getSimpleUser());
                    else
                        out.writeObject(null);//FIXME
                    out.flush();
                } catch (IOException e) {
                    connected = false;
                } catch (ClassNotFoundException e) {
                    connected = false;
                    e.printStackTrace();
                }
            }

        });
        thread.setDaemon(true);
        thread.start();
    }

    private static void processMessage(Message message) {
        Thread thread = new Thread(() -> {
            for (Long target : message.targets) {
                Optional<User> user = db.findUserByID(target);
                if (user.isPresent() && user.get().isOnline()) {
                    try {
                        user.get().getOutput().writeObject(message);
                        user.get().getOutput().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}