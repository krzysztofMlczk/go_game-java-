package com.tearsvalley.serializable;

import java.io.Serializable;

import com.tearsvalley.data.Command;


public interface MessageInterface extends Serializable {
    
    public Command getCommand();
}