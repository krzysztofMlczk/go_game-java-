package com.tearsvalley;

import javafx.application.Application;
import javafx.application.Platform;

import java.util.ArrayList;

import com.tearsvalley.client.Client;
import com.tearsvalley.controllers.BoardWindowController;
import com.tearsvalley.data.Color;
import com.tearsvalley.data.Gamemode;
import com.tearsvalley.data.Point;
import com.tearsvalley.serializable.MessageInterface;
import com.tearsvalley.serializable.MoveMessage;
import com.tearsvalley.serializable.PassMessage;
import com.tearsvalley.serializable.QuitMessage;


public class Connector {

    private static Client client;
    private static BoardWindowController bWindow;
    private static Color turnColor;
    private static Color clientColor;
    public static Gamemode thisgamemode;

    private static int captives = 0;
    private static int placedStones = 0;

    public static void start() {
        client = new Client();
        clientColor = client.getColor();
        Application.launch(StartGraphicMode.class);

    }

    public static void setBoardControllerInstance(BoardWindowController bWindowController) {
        bWindow = bWindowController;
    }

     // BoardWindowController -> Client

     public static Color getClientColor() {
        return client.getColor();
    }

    public static void sendMove (Point point, Color stoneColor) {
        MessageInterface move = new MoveMessage(point, stoneColor);
        client.sendMessageToServer(move);
    }

    public static void sendPass() {
        MessageInterface pass = new PassMessage();
        client.sendMessageToServer(pass);
    }

    private static void enableStartButton() {
        Platform.runLater(() -> bWindow.enableStartButton());
    }

    public static void sendQuitMessage() {
        MessageInterface quit = new QuitMessage();
        client.sendMessageToServer(quit);
    }

    //Client -> BoardWindowController
    public static void placeStone(Point point, Color color) {
        bWindow.setStoneOnBoard(point, color);
        if(color == clientColor) {
            incrementStones();
        }
        setPlacedStonesLabel();
    }

    public static void deleteStones(ArrayList<Point> fieldsToDelete, Color delColor) {
        for(Point point: fieldsToDelete) {
            bWindow.deleteStone(point);
            if(delColor != clientColor) {
                decrementStones();
            }
            else {
                incrementCaptives();
            }
        }

        setCaptivesLabel();
        setPlacedStonesLabel();
    }

    public static void showStats(int territory, int score) {
        Platform.runLater(() -> {bWindow.showTerritoryLabels(territory);
            bWindow.showScoreLabels(score);});
    }


    //MenuWindowController -> Client
    public static void sendGamemode(Gamemode gamemode) {
        thisgamemode = gamemode;
        client.sendIntialMessage(gamemode);
    }


    //Cycle -> BoardWindowController
    public static void setErrLabel(String errmessage) {
        Platform.runLater(() -> bWindow.setErrorLabel(errmessage));
    }

    public static void disableBoard() {
        Platform.runLater(() -> bWindow.disableBoard());
    }
    public static void enableBoard() {
        Platform.runLater(() -> bWindow.enableBoard());
    }

    public static void decrementCaptives() {
        captives--;
    }
    public static void incrementCaptives() {
        captives++;
    }
    public static void decrementStones() {
        placedStones--;
    }
    public static void incrementStones() {
        placedStones++;
    }

    public static void setPlacedStonesLabel() {
        bWindow.setplacedStonesAmountLabel(placedStones);
    }

    public static void setCaptivesLabel() {
        bWindow.setCaptivesLabel(captives);
    }



    public static void waitForAnotherPlayer() {
        Platform.runLater(() -> client.receiveStartGameMessage());

        enableStartButton();
        setErrLabel("Press start to play");
    }

    public static Color getTurnColor() {
        return turnColor;
    }

    public static void setTurnColor(Color color) {
        turnColor = color;
    }

    public static Client getClient () {
        return client;
    }

    public static void closeConnection() {
        client.close();
    }
}