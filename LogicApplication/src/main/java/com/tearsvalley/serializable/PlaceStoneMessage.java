package com.tearsvalley.serializable;

import com.tearsvalley.data.Color;
import com.tearsvalley.data.Command;
import com.tearsvalley.data.Point;

public class PlaceStoneMessage implements MessageInterface {

    private static final long serialVersionUID = 1L;
    private Command command = Command.PLACESTONE;
    private Point point;

    private Color playerColor;

    public PlaceStoneMessage(Point point, Color playerColor) {
        this.point = point;
        this.playerColor = playerColor;
    }


    @Override
    public Command getCommand() {
        return command;
    }

    public Point getPoint() {
        return point;
    }
    public Color getColor() {
        return playerColor;
    }
}