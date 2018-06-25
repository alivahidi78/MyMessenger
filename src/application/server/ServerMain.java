package application.server;

import application.server.modules.Network;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
    public static void main(String[] args) {
        try {
            Network.processRequests(new ServerSocket(8085));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
