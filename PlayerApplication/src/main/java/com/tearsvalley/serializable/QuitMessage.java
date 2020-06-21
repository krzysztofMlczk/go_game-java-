package com.tearsvalley.serializable;

import com.tearsvalley.data.Command;

public class QuitMessage implements MessageInterface {

    private static final long serialVersionUID = 1L;
    private Command command = Command.QUIT;

    @Override
    public Command getCommand() {
        return command;
    }
}