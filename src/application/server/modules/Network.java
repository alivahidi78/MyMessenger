package application.server.modules;

import application.util.answer.SignInAcceptedAnswer;
import application.util.answer.SignInDeniedAnswer;
import application.util.answer.SignUpAcceptedAnswer;
import application.util.answer.SignUpDeniedAnswer;
import application.util.message.Message;
import application.util.request.ConstantConnectionRequest;
import application.util.request.Request;
import application.util.request.SignUpRequest;
import application.util.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

/**
 * Handles the connection between the clients and the server.
 * */
public class Network {
    private static ServerSocket serverSocket;
    private static Database db = Database.getInstance();

    private static void handleCCRequest(Request request, ObjectInputStream in, ObjectOutputStream out) {
        ConstantConnectionRequest ccRequest = (ConstantConnectionRequest) request;
        String username = ccRequest.username;
        String password = ccRequest.password;
        Optional<User> user = db.findUserByUsername(username);
        try {
            if (!user.isPresent() || !user.get().passwordEquals(password)) {
                out.writeObject(new SignInDeniedAnswer());
            } else {
                out.writeObject(new SignInAcceptedAnswer(user.get()));
                createConstantConnection(in, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handleSignUpRequest(Request request, ObjectInputStream in, ObjectOutputStream out) {
        SignUpRequest suRequest = (SignUpRequest) request;
        String name = suRequest.name;
        String username = suRequest.username;
        String password = suRequest.password;
        Optional<User> duplicate = db.findUserByUsername(username);
        try {
            if (duplicate.isPresent()) {
                out.writeObject(new SignUpDeniedAnswer());
            } else {
                out.writeObject(new SignUpAcceptedAnswer());
                db.addUser(new User(name,username,password));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void createConstantConnection(ObjectInputStream in, ObjectOutputStream out) {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Message message = (Message) in.readObject();
                    //TODO process message
                } catch (IOException e) {
                    e.printStackTrace();
                    e.getMessage(); //TODO disconnected
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //TODO
        });
        thread.setDaemon(true);
        thread.start();
    }
}