package com.tearsvalley.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.tearsvalley.data.Color;
import com.tearsvalley.data.Gamemode;
import com.tearsvalley.game.*;

public class Server {
    private static final int port = 5000;      
    private static ArrayList<Player> players = new ArrayList<>();
    Game game;
    
    private ServerSocket serverSocket = null;

    private static Gamemode gamemode = Gamemode.NOTCHOSENYET;

    public Server() {
        try {
            serverSocket = new ServerSocket(port);

            } catch(IOException ex) {
                System.out.println(ex);
            }
    }

    public boolean waitForThePlayer() {
        Socket playerSocket = null;
        try {

            playerSocket = serverSocket.accept();
    
            if(players.size() == 0) {
                players.add(new Player(playerSocket, Color.BLACK));
            }else if(players.size() == 1) 
                players.add(new Player(playerSocket, Color.WHITE));      
            } catch (IOException e) {
                e.printStackTrace();
                return false; // return false when failed
        }
        return true;
    }


    public void start() throws Exception{
        //not enough clients to play multiplayer - wait for the second player
        if(gamemode == Gamemode.MULTIPLAYER && players.size() < 2) {
            waitForThePlayer();
            start();
        }
        else if (gamemode == Gamemode.MULTIPLAYER && players.size() == 2) {
            while(players.get(1).getGamemode() != gamemode); 
            
            game = new Multiplayer(players);
            game.setPlayers(); //setting up players for the game
            game.runGame();
            }
        else if (gamemode == Gamemode.SINGLEPLAYER) {
            game = new Singleplayer(players.get(0));
            game.setPlayers();
            game.runGame();
        }
    }

    public static int getPlayersAmount() {
        return players.size();
    }

    public static Gamemode getGamemode () {
        return gamemode;
    }

    public static void setGamemode(Gamemode chosenGamemode) {
        gamemode = chosenGamemode;
    }
    
    public void close() throws IOException {
    	serverSocket.close();
    }
    
}
