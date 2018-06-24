package application.client.modules;

import application.util.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;

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
                    Cache.chats.computeIfAbsent(message.sender, k -> new LinkedList<>());
                    Cache.chats.get(message.sender).add(message);
                    GraphicEventHandler.reloadCache(message);
                } catch (IOException e) {
                    connected = false;
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
