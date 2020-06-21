package com.tearsvalley.serializable;

import com.tearsvalley.data.Color;
import com.tearsvalley.data.Command;
import com.tearsvalley.data.Point;

public class MoveMessage implements MessageInterface {

    private static final long serialVersionUID = 1L;
    private Command command = Command.MOVE;

    private Point point;
    private Color stoneColor;

    public MoveMessage(Point point, Color stoneColor) {
        this.point = point;
        this.stoneColor = stoneColor;
    }

    @Override
    public Command getCommand() {
        return command;
    }

    public Point getPoint() {
        return point;
    }

    public Color getStoneColor() {
        return stoneColor;
    }
}