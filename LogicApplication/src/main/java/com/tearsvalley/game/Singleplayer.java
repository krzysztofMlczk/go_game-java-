package com.tearsvalley.game;

import java.io.IOException;

import com.tearsvalley.bot.Bot;
import com.tearsvalley.server.Player;

//class which handles singleplayer game
public class Singleplayer extends Game{
    private Player player; //only one player which will play singleplayer mode

    public Singleplayer (Player player) {
        super();
        this.player = player;
    }

    @Override
    public void runGame() {
            new Thread(this.player).start(); //we are starting player thread and somehow connect it with bot which doesn't exist yet

            player.setBoardService(boardService);

            try{
                this.player.sendStartGameMessage();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    @Override
    public void setPlayers() {
        setCurrentPlayer(this.player);
    }
}