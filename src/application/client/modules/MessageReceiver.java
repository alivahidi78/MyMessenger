package application.client.modules;

import application.util.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class MessageReceiver {
    private ObjectInputStream in;
    private Thread thread;
    private volatile boolean connected;

    public MessageReceiver(ObjectInputStream input) {
        this.in = input;
        thread = new Thread(() -> {
            connected = true;
            while (connected) {
                try {
                    Message message = (Message) in.readObject();
                    //TODO process message
                } catch (IOException e) {
                    connected = false;
                    e.printStackTrace();
                    e.getMessage(); //TODO disconnected
                } catch (ClassNotFoundException e) {
                    connected = false;
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        connected = false;
    }
}
