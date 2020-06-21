package com.tearsvalley.serializable;

import java.io.Serializable;

import com.tearsvalley.data.Color;
import com.tearsvalley.data.Gamemode;

public class InitialMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Gamemode gamemode = null; //server side will need gamemode to correctly handle the game
    private Color playerColor = Color.NAC;

    public InitialMessage (Gamemode gamemode) {
        this.gamemode = gamemode;
    }

    public InitialMessage(Color playerColor) {
        this.playerColor = playerColor;
    }
    
    public Gamemode getGamemode () {
        return this.gamemode;
    }

    public Color getPlayerColor() {
        return this.playerColor;
    }
}