package application.client.modules;

import application.util.answer.Answer;
import application.util.answer.ConnectionFailedAnswer;
import application.util.request.Request;
import application.util.request.RequestType;
import application.util.user.SimpleUser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Network {
    private static String host;
    private static int port;
    private static ObjectOutputStream constantOutput;
    private static ObjectInputStream constantInput;
    private static Receiver constantReceiver;
    private static ObjectOutputStream searchOutput;
    private static ObjectInputStream searchInput;

    private static Socket getNewSocket() throws IOException {
        return new Socket(host, port);
    }

    public static void initialize(String host, int port) {
        Network.host = host;
        Network.port = port;
    }

    private static void startConstantConnection(ObjectInputStream in, ObjectOutputStream out) {
        constantInput = in;
        constantOutput = out;
        constantReceiver = new Receiver(in);
        constantReceiver.start();
        //TODO
    }

    private static void startSearchConnection(ObjectInputStream in, ObjectOutputStream out) {
        searchInput = in;
        searchOutput = out;
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
            if (request.type == RequestType.CONSTANT_CONNECTION && answer.requestAccepted)
                startConstantConnection(in, out);
            if (request.type == RequestType.SEARCH_CONNECTION && answer.requestAccepted)
                startSearchConnection(in, out);
            return answer;
        } catch (IOException e) {
            return new ConnectionFailedAnswer();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        finally {
//            if (request.type != RequestType.CONSTANT_CONNECTION && socket != null) {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return null;//TODO remove
    }

    public static List<SimpleUser> getSearchResult(String s) {
        try {
            searchOutput.writeObject(s);
            return (List<SimpleUser>) searchInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
