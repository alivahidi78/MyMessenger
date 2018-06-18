package application.client.modules;

import application.util.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Receiver {
    private ObjectInputStream in;
    private Thread thread;

    public Receiver(ObjectInputStream input) {
        this.in = input;
        thread= new Thread(()->{
            while (true){
                try {
                    Message message =(Message) in.readObject();
                    //TODO process message
                } catch (IOException e) {
                    e.printStackTrace();
                    e.getMessage(); //TODO disconnected
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
    }

    public void start(){
        thread.start();
    }
}
