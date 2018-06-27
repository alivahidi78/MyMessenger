package application.client.modules;

import application.util.answer.Answer;
import application.util.answer.AnswerType;
import application.util.answer.ConnectionFailedAnswer;
import application.util.answer.SignInAcceptedAnswer;
import application.util.message.FileMessage;
import application.util.message.Message;
import application.util.request.*;
import application.util.user.Info;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Network {
    private static String host;
    private static int port;
    private static ObjectOutputStream messagingOutput;
    private static ObjectInputStream messagingInput;
    private static MessageReceiver messageReceiver;
    private static ObjectOutputStream searchOutput;
    private static ObjectInputStream searchInput;
    private static ObjectOutputStream userInfoOutput;
    private static ObjectInputStream userInfoInput;

    private static Socket getNewSocket() throws IOException {
        return new Socket(host, port);
    }

    public static void initialize(String host, int port) {
        Network.host = host;
        Network.port = port;
    }

    public static Answer initiateConnections(String username, String password) {
        Answer answer = Network.request(new MessagingConnectionRequest(username, password));
        if (answer.type == AnswerType.SIGN_IN_ACCEPTED) {
            Cache.setCurrentUser(((SignInAcceptedAnswer) answer).user);
            Cache.loadChats(((SignInAcceptedAnswer) answer).user.getChats());
            Answer userInfoAnswer = Network.request(new UserInfoConnectionRequest(Cache.getCurrentUser().getUsername(),
                    Cache.getCurrentUser().getPassword()));
            Answer SearchConAnswer = Network.request(new SearchConnectionRequest(
                    Cache.getCurrentUser().getUsername(), Cache.getCurrentUser().getPassword()));
            messageReceiver.start();
        }
        return answer;
    }

    private static void startMessagingConnection(ObjectInputStream in, ObjectOutputStream out) {
        messagingInput = in;
        messagingOutput = out;
        messageReceiver = new MessageReceiver(in);
    }

    private static void startSearchConnection(ObjectInputStream in, ObjectOutputStream out) {
        searchInput = in;
        searchOutput = out;
    }

    private static void startUserInfoConnection(ObjectInputStream in, ObjectOutputStream out) {
        userInfoInput = in;
        userInfoOutput = out;
    }

    public static void uploadProfilePicture(String path, LongProperty totalDownload) {

    }

    public static Answer request(Request request) {
        //IS CONCURRENT???
        ObjectInputStream in;
        ObjectOutputStream out;
        Socket socket = null;
        try {
            socket = getNewSocket();
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(request);
            Answer answer = (Answer) in.readObject();
            if (request.type == RequestType.MESSAGING_CONNECTION && answer.requestAccepted)
                startMessagingConnection(in, out);
            if (request.type == RequestType.SEARCH_CONNECTION && answer.requestAccepted)
                startSearchConnection(in, out);
            if (request.type == RequestType.USER_INFO_CONNECTION && answer.requestAccepted)
                startUserInfoConnection(in, out);
            return answer;
        } catch (IOException e) {
            return new ConnectionFailedAnswer();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (!request.isConstant && socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;//TODO remove
    }

    public static List<Info> getSearchResult(String s) throws IOException {
        try {
            searchOutput.writeObject(s);
            searchOutput.flush();
            List<Info> list = (List<Info>) searchInput.readObject();
            list.forEach(Cache::loadUserInfoToCache);
            return (list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();//TODO Error
    }

    synchronized static Info getUserInfoFromServer(long id) throws IOException {
        try {
            userInfoOutput.writeObject(id);
            userInfoOutput.flush();
            return (Info) userInfoInput.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void disconnect() throws IOException {
        messageReceiver.stop();
        messagingOutput.close();
        userInfoInput.close();
        userInfoOutput.close();
        searchOutput.close();
        searchInput.close();
    }

    static void sendMessage(Message message) throws IOException {
        messagingOutput.writeObject(message);
        messagingOutput.flush();
    }

    static void sendFile(FileMessage message, File file, DoubleProperty property) throws Exception {
        Request request = new UploadFileRequest(Cache.getCurrentUser().getUsername(),
                Cache.getCurrentUser().getPassword(), file.length(), file.getName());
        ObjectInputStream in;
        ObjectOutputStream out;
        Socket socket = null;
        Answer answer;
        socket = getNewSocket();
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(request);
        answer = (Answer) in.readObject();
        FileHandler.sendFileToStream(out, file.getAbsolutePath(), property);
        sendMessage(message);
    }
}
