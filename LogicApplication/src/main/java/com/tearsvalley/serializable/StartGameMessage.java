package com.tearsvalley.serializable;

import com.tearsvalley.data.Color;
import com.tearsvalley.data.Command;

public class StartGameMessage implements MessageInterface {

    private static final long serialVersionUID = 1L;
    private Command command = Command.STARTGAME;
    private Color turnColor = Color.BLACK; //turncolor informs client if its his turn (if the color doesn't match the clien's color - its not his turn)

    @Override
    public Command getCommand() {
        return command;
    }
    public Color getTurnColor() {
        return turnColor;
    }
}