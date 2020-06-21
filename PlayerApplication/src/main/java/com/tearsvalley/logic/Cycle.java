package com.tearsvalley.logic;

import com.tearsvalley.Connector;
import com.tearsvalley.client.Client;
import com.tearsvalley.data.Color;
import com.tearsvalley.data.Point;
import com.tearsvalley.serializable.MessageInterface;

public class Cycle {
    static Client client = Connector.getClient();
    
    public static void yourTurnCycle(Point point, Color clientColor) {   
            Connector.disableBoard();
            Connector.sendMove(point, clientColor); //we send the proposal
        //we have sent the move proposal so now we have to listen if server accepts this move

        MessageInterface serverResponse = client.readMessageFromServer();
        //we want to handle this message from server
        /*
         * if handle returns 0 it means, that the move was not vaild, so if we do !0 
         * then we execute return - exiting the Cycle, and letting the player to choose other field by unblocking panes
        */
        if(!MessageHandler.handle(serverResponse)){
            return; 
        }

        MessageInterface deleteStones = client.readMessageFromServer();
        MessageHandler.handle(deleteStones);


        notYourTurnCycle();

    }

    public static void notYourTurnCycle() {

        Connector.disableBoard();
        Connector.setErrLabel("Not your turn");

        MessageInterface serverResponse = client.readMessageFromServer();
        if(!MessageHandler.handle(serverResponse)) {

            return;
        }
        MessageInterface deleteStones = client.readMessageFromServer();
        MessageHandler.handle(deleteStones);
        Connector.enableBoard();
        Connector.setErrLabel("Your Turn");
    }

    public static void singleCycle(Point point, Color clientColor) {
        Connector.disableBoard();
        Connector.sendMove(point, clientColor);

        MessageInterface serverResponse = client.readMessageFromServer();
        
        if(!MessageHandler.handle(serverResponse)){
            return; 
        }

        MessageInterface deleteStones = client.readMessageFromServer();
        MessageHandler.handle(deleteStones);

        MessageInterface bStone = client.readMessageFromServer();
        MessageHandler.handle(bStone);

        MessageInterface bdStone = client.readMessageFromServer();
        MessageHandler.handle(bdStone);

        Connector.enableBoard();
    }
}