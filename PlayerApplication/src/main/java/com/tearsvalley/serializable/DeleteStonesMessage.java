package com.tearsvalley.serializable;

import java.util.ArrayList;

import com.tearsvalley.data.Color;
import com.tearsvalley.data.Command;
import com.tearsvalley.data.Point;

public class DeleteStonesMessage implements MessageInterface {

    private static final long serialVersionUID = 1L;
    private Command command = Command.DELETESTONES;
    private ArrayList<Point> fieldsToDelete;
    private Color color;

    public DeleteStonesMessage (ArrayList<Point> fieldsToDelete, Color color) {
        this.fieldsToDelete = fieldsToDelete;
        this.color = color;
    }

    @Override
    public Command getCommand() {
        return command;
    }
    public ArrayList<Point> getFieldToDelete() {
        return fieldsToDelete;
    }

    public Color getColor() {
        return color;
    }
    
}