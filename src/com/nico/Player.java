package com.nico;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Player extends Thread {
    String player;
    Socket socket;
    Thread thread;
    ObjectOutputStream output;
    BufferedReader input;
    int playerNr;

    public int getPlayerNr() {
        return playerNr;
    }

    public Player(String player, Socket socket, int playerNr) {
        this.playerNr = playerNr;
        this.player = player;
        this.socket = socket;
        try{
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread = new Thread(this);
    }

    public void sendMessageToClient(Object o){
        try {
            output.reset();
            output.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAnswer(){
        String s = "";
        try {
            while((s = input.readLine()) != null){
                return s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
