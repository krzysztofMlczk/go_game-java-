package com.tearsvalley.logic;

import com.tearsvalley.Connector;
import com.tearsvalley.data.Command;
import com.tearsvalley.serializable.*;

import javafx.application.Platform;

public class MessageHandler {

    public static boolean handle(MessageInterface message) {
        Command command = message.getCommand();

		if(command == Command.PLACESTONE) {
            PlaceStoneMessage plstone = (PlaceStoneMessage) message;
            Platform.runLater(() -> Connector.placeStone(plstone.getPoint(), plstone.getColor()));
        }
        else if(command == Command.PASSED) {
            Connector.enableBoard();
            Connector.setErrLabel("Your Turn");
            return false;
        }
        else if (command == Command.NOTVALIDMOVE) {
            Connector.enableBoard();
            Connector.setErrLabel("Bad Move");
            return false;
        }
        else if(command == Command.ENDGAME) {
            EndMessage endmessage = (EndMessage) message;
            Connector.setErrLabel(endmessage.getWinner()+" won");
            Connector.showStats(endmessage.getTerritory(), endmessage.getScore());
            Connector.disableBoard();
        }
        else if(command == Command.DELETESTONES) {
            DeleteStonesMessage delStones = (DeleteStonesMessage) message;
            if(delStones.getFieldToDelete() != null)
            Platform.runLater(() -> Connector.deleteStones(delStones.getFieldToDelete(), delStones.getColor()));//w metodzie deleteSotnes w connectorze robimy for each i wywolujemyw  kazdym przejciu metode z board window, ktora usuwa kamienie
        }
        else if(command == Command.QUIT) {
            Connector.disableBoard();
            Connector.setErrLabel("You won, enemy withdrew");
        }
        else if(command == Command.CLOSECONNECTION) {
            Connector.closeConnection();
        }

            return true;
    }
}