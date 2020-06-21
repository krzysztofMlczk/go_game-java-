package com.tearsvalley.serializable;

import com.tearsvalley.data.Command;

public class PassMessage implements MessageInterface {

    private static final long serialVersionUID = 1L;
    private Command command = Command.PASSED;

    @Override
    public Command getCommand() {
        return command;
    }
}