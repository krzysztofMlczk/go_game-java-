package com.tearsvalley.game;

import java.io.IOException;
import java.util.ArrayList;

import com.tearsvalley.data.Color;
import com.tearsvalley.server.Player;

//class which handles multiplayer game
public class Multiplayer extends Game{
    ArrayList <Player> players; //arraylist of connected players

    public Multiplayer (ArrayList<Player> players) {
        super();
        this.players = players;
    }

    @Override
    public void runGame() {
        for (Player p: players){
            new Thread(p).start(); // we start thread for each player (we have two of those anyway)

            p.setBoardService(boardService);
            
            try{
                p.sendStartGameMessage();
            } catch(IOException e) {
                e.printStackTrace();
            }
            System.out.println("Wystartowano gracza "+p.getPlayerColor());
        }
    }

    @Override 
    public void setPlayers () {
        for (Player p : players) {
            if(p.getPlayerColor() == Color.BLACK)
                setCurrentPlayer(p);
            else {
                p.setOpponent(getCurrentPlayer());
                p.getOpponent().setOpponent(p);// if u re white you re an opponent of your opponent 
            }
        }
    }
}