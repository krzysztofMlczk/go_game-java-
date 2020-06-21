package com.tearsvalley.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.tearsvalley.Connector;
import com.tearsvalley.data.Color;
import com.tearsvalley.data.Command;
import com.tearsvalley.data.Gamemode;
import com.tearsvalley.serializable.InitialMessage;
import com.tearsvalley.serializable.MessageInterface;
import com.tearsvalley.serializable.StartGameMessage;

public class Client {

    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;

    private Color playerColor;

    private static final int port = 5000;
    private static final String address = "localHost";
    
    public boolean connected = false;

    public Client() {

        try {
            socket = new Socket(address, port);
            System.out.println("Connected to server");

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            
            connected = true;

            receiveInitialMessage();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    // Czytamy kolor od serwera
    private void receiveInitialMessage() throws ClassNotFoundException, IOException {
        InitialMessage im = (InitialMessage) inputStream.readObject();

        this.playerColor = im.getPlayerColor();
    }

    // Wysyłamy gamemode do serwera
    public void sendIntialMessage(Gamemode gamemode) {
        try {
            outputStream.writeObject(new InitialMessage(gamemode));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveStartGameMessage() {
        MessageInterface startGame;
        try {
            startGame = (MessageInterface) inputStream.readObject();

            if (startGame.getCommand() == Command.STARTGAME) {
                StartGameMessage startMessage = (StartGameMessage) startGame;
                Connector.setTurnColor(startMessage.getTurnColor());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessageToServer(MessageInterface mi) {
        try {
            outputStream.writeObject(mi);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MessageInterface readMessageFromServer() {
        try {
            return (MessageInterface) inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Color getColor() {
        return playerColor;
    }
}