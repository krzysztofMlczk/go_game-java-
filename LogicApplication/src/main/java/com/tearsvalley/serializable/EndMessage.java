package com.tearsvalley.serializable;

import com.tearsvalley.data.Color;
import com.tearsvalley.data.Command;

public class EndMessage implements MessageInterface{

    private static final long serialVersionUID = 1L;
    
    private Command command = Command.ENDGAME;
    private Color winner; 
    private int territory;
    private int score;

    public EndMessage(Color winner, int territory, int score) {
        this.winner = winner;
        this.territory = territory;
        this.score = score;
    }


    @Override
    public Command getCommand() {

        return command;
    }

    public Color getWinner() {
        return winner;
    }
    public int getTerritory() {
        return territory;
    }
    public int getScore() {
        return score;
    }

}