package application.server.modules;

import application.util.answer.*;
import application.util.message.Message;
import application.util.request.ConstantConnectionRequest;
import application.util.request.Request;
import application.util.request.SearchConnectionRequest;
import application.util.request.SignUpRequest;
import application.util.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Handles the connection between the clients and the server.
 */
public class Network {
    private static ServerSocket serverSocket;
    private static Database db = Database.getInstance();
    //TODO make concurrent
    private static ArrayList<Connection> onlineUsers = new ArrayList<>();

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
        String username = suRequest.username;
        String password = suRequest.password;
        Optional<User> duplicate = db.findUserByUsername(username);
        if (duplicate.isPresent()) {
            out.writeObject(new SignUpDeniedAnswer());
        } else {
            out.writeObject(new SignUpAcceptedAnswer());
            db.addUser(new User(name, username, password));
        }
    }

    private static void handleSearchRequest(Request request, ObjectInputStream in, ObjectOutputStream out) throws IOException {
        SearchConnectionRequest scRequest = (SearchConnectionRequest) request;
        Connection connection = new Connection(in, out, db.findUserByUsername(scRequest.username).get());
        out.writeObject(new ConnectedAnswer());
        createSearchConnection(connection);
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
                            //TODO - Important
                            handleSearchRequest(request, in, out);
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
        Thread thread = new Thread(() -> {
            boolean connected = true;
            while (connected) {
                try {
                    Message message = (Message) in.readObject();
                    //TODO process message
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
                    out.writeObject(db.searchFor(search));
                } catch (IOException e) {
                    connected = false;
                    connection.disconnect();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}