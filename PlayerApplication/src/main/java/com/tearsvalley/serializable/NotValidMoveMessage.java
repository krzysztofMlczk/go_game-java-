package com.tearsvalley.serializable;

import com.tearsvalley.data.Command;

public class NotValidMoveMessage implements MessageInterface {

    private static final long serialVersionUID = 1L;
    private Command command = Command.NOTVALIDMOVE;

    @Override
    public Command getCommand() {
        return command;
    }
    
}