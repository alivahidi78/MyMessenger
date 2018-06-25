package application.client.modules;

import application.util.answer.Answer;
import application.util.answer.ConnectionFailedAnswer;
import application.util.message.TextMessage;
import application.util.request.Request;
import application.util.request.RequestType;
import application.util.user.SimpleUser;

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

    private static void startMessagingConnection(ObjectInputStream in, ObjectOutputStream out) {
        messagingInput = in;
        messagingOutput = out;
        messageReceiver = new MessageReceiver(in);
        messageReceiver.start();
        //TODO
    }

    private static void startSearchConnection(ObjectInputStream in, ObjectOutputStream out) {
        searchInput = in;
        searchOutput = out;
    }

    private static void startUserInfoConnection(ObjectInputStream in, ObjectOutputStream out) {
        userInfoInput = in;
        userInfoOutput = out;
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

    public static List<SimpleUser> getSearchResult(String s) throws IOException {
        try {
            searchOutput.writeObject(s);
            searchOutput.flush();
            //noinspection unchecked
            return ((List<SimpleUser>) searchInput.readObject());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();//TODO Error
    }

    public synchronized static SimpleUser getUserInfo(long id) throws IOException {
        try {
            userInfoOutput.writeObject(id);
            userInfoOutput.flush();
            return (SimpleUser) userInfoInput.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void disconnect() throws IOException {
        messageReceiver.stop();
        messagingOutput.close();
        searchOutput.close();
        searchInput.close();
    }

    public static void sendMessage(TextMessage message) throws IOException {
        messagingOutput.writeObject(message);
        messagingOutput.flush();
    }
}
