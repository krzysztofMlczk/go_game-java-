package com.tearsvalley.serializable;

import com.tearsvalley.data.Command;

public class CloseConnection implements MessageInterface {
    private static final long serialVersionUID = 1L;

    private Command command = Command.CLOSECONNECTION;

    @Override
    public Command getCommand() {
        return command;
    }
    
}