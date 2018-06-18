package application.client.modules;

import application.util.answer.Answer;
import application.util.answer.ConnectionFailedAnswer;
import application.util.request.Request;
import application.util.request.RequestType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Network {
    private static String host;
    private static int port;
    private static ObjectOutputStream constantOutput;
    private static ObjectInputStream constantInput;
    private static Receiver receiver;

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
        receiver = new Receiver(in);
        receiver.start();
        //TODO
    }

    public static Answer request(Request request) {
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
            return answer;
        } catch (IOException e) {
            return new ConnectionFailedAnswer();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (request.type != RequestType.CONSTANT_CONNECTION && socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;//TODO remove
    }
}
