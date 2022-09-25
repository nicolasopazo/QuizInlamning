package com.nico;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public Server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(55556);

        try {
            while (true) {
                Player player1 = new Player("player1",serverSocket.accept(), 0);
                Player player2 = new Player("player2",serverSocket.accept(), 1);
                System.out.println("Created game");
                player1.sendMessageToClient("player1");
                player2.sendMessageToClient("player2");
                Game game = new Game(player1, player2);
                game.start();
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    public static void main(String[]args) throws IOException {
        Server server = new Server();
    }
}

