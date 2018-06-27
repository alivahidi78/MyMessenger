package application.server.modules;

import application.server.modules.connection.MessagingConnection;
import application.server.modules.connection.SearchConnection;
import application.server.modules.connection.UserInfoConnection;
import application.util.answer.*;
import application.util.message.Message;
import application.util.message.info.GroupAdditionInfoMessage;
import application.util.request.*;
import application.util.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Optional;

/**
 * Handles the connection between the clients and the server.
 */
public class Network {
    private static ServerSocket serverSocket;
    private static Database db = Database.getInstance();

    private static void handleMConnectionRequest(Request request, ObjectInputStream in, ObjectOutputStream out) {
        MessagingConnectionRequest MCRequest = (MessagingConnectionRequest) request;
        String username = MCRequest.username;
        String password = MCRequest.password;
        Optional<User> user = db.findUserByUsername(username);
        try {
            if (!user.isPresent() || !user.get().passwordEquals(password)) {
                out.writeObject(new SignInDeniedAnswer());
            } else {
                MessagingConnection connection = new MessagingConnection(in, out, user.get());
                out.writeObject(new SignInAcceptedAnswer(user.get()));
                connection.connect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleSearchRequest(Request request, ObjectInputStream in, ObjectOutputStream out) {
        SearchConnectionRequest scRequest = (SearchConnectionRequest) request;
        Optional<User> user = db.findUserByUsername(scRequest.username);
        try {
            if (user.isPresent()) {
                SearchConnection connection = new SearchConnection(in, out, user.get());
                out.writeObject(new ConnectedAnswer());
                connection.connect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleUserInfoRequest(Request request, ObjectInputStream in, ObjectOutputStream out) {
        UserInfoConnectionRequest uiRequest = (UserInfoConnectionRequest) request;
        try {
            out.writeObject(new ConnectedAnswer());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserInfoConnection connection = new UserInfoConnection(in, out);
        connection.connect();
    }

    private static void handleSignUpRequest(Request request, ObjectOutputStream out) {
        SignUpRequest suRequest = (SignUpRequest) request;
        String name = suRequest.name;
        String username = suRequest.username.toLowerCase();
        String password = suRequest.password;
        Optional<User> duplicate = db.findUserByUsername(username);
        try {
            if (duplicate.isPresent()) {
                out.writeObject(new SignUpDeniedAnswer());
            } else {
                out.writeObject(new SignUpAcceptedAnswer());
                db.createNewUser(name, username, password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleUploadRequest(Request request, ObjectInputStream in, ObjectOutputStream out) {
        UploadFileRequest r = (UploadFileRequest) request;
        try {
            out.writeObject(new UploadFileAnswer());
            FileHandler.saveFileFromStream(in, "data/" + r.username + "/"
                    + ((UploadFileRequest) request).fileName);//TODO change,moveUp exception
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handlerGroupCreationRequest(Request request, ObjectOutputStream out) {
        GroupCreationRequest gcRequest = (GroupCreationRequest) request;
        Thread thread = new Thread(() -> {
            long groupID = db.createGroup(gcRequest.name, gcRequest.members);
            gcRequest.members.forEach((user1) -> {
                Message message = new GroupAdditionInfoMessage(user1, groupID, new Date());
                MessagingConnection.processMessage(message);
            });
        });
        thread.setDaemon(true);
        thread.start();
        try {
            out.writeObject(new AcceptedAnswer());
            out.flush();
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
                        case MESSAGING_CONNECTION:
                            handleMConnectionRequest(request, in, out);
                            break;
                        case SEARCH_CONNECTION:
                            handleSearchRequest(request, in, out);
                            break;
                        case USER_INFO_CONNECTION:
                            handleUserInfoRequest(request, in, out);
                            break;
                        case SIGN_UP:
                            handleSignUpRequest(request, out);
                            break;
                        case GROUP_CREATION:
                            handlerGroupCreationRequest(request, out);
                        case UPLOAD_FILE:
                            handleUploadRequest(request, in, out);
                            break;
                        case DOWNLOAD_FILE:
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
}