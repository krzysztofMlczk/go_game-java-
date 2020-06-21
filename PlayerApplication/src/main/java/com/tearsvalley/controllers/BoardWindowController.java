package com.tearsvalley.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.tearsvalley.data.Color;
import com.tearsvalley.data.Gamemode;
import com.tearsvalley.data.Point;
import com.tearsvalley.logic.Cycle;
import com.tearsvalley.Connector;
import com.tearsvalley.Main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class BoardWindowController {

    //additional variables
    Color clientColor;
    Gamemode gamemode;

    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView button_shutdown;

    @FXML
    private GridPane grid;

    @FXML
    private Button button_pass;

    @FXML
    private ImageView blackColorIndicator;

    @FXML
    private ImageView whiteColorIndicator;

    @FXML
    private Label captivesAmountLabel;

    @FXML
    private Label placedStonesAmountLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Button startButton;

    @FXML
    private Label territoryLabel;

    @FXML
    private Label scoreLabe;

    @FXML
    private Label territoryAmount;

    @FXML
    private Label scoreAmount;


    @FXML
    void passButtonClicked(MouseEvent event) {

        if(gamemode == Gamemode.MULTIPLAYER) {
            Connector.sendPass();
            Thread thread = new Thread(new Runnable(){
            
                @Override
                public void run() {
                    if(gamemode == Gamemode.MULTIPLAYER)
                        Cycle.notYourTurnCycle();
                }
            });

            thread.setDaemon(true);
            thread.start();
        }
        else if(gamemode == Gamemode.SINGLEPLAYER) {
            setErrorLabel("Game ended");
            disableBoard();
        }
    }

    @FXML
    void shutdownButtonClicked(MouseEvent event) {
        Connector.sendQuitMessage();
        System.exit(0);
    }

    @FXML
    void startButtonClicked(MouseEvent event) {
        //Initializing the turn cycle
        anchorPane.getChildren().remove(startButton);

        if(Connector.getTurnColor() == clientColor){
            setErrorLabel("Your turn");
            enableBoard();
        }
        else if(Connector.getTurnColor() != clientColor){
            //what happens when its not your turn
            setErrorLabel("Not your turn");
            
            Thread thread = new Thread(new Runnable(){
            
                @Override
                public void run() {
                    Cycle.notYourTurnCycle();
                }
            });

            thread.setDaemon(true);
            thread.start();
        }
    }

    //Additional methods
    
        //setters:
        public void setErrorLabel (String errorMessage) {
            this.errorLabel.setText(errorMessage);
        }
        
        public void setplacedStonesAmountLabel (int amount) {
            this.placedStonesAmountLabel.setText(Integer.toString(amount));
        }

        public void setCaptivesLabel(int amount) {
            this.captivesAmountLabel.setText(Integer.toString(amount));
        }
        
        // sets Color indicator so the player knows which color he has
        public void setColorIndicator () { 
            if (this.clientColor == Color.BLACK) {
                blackColorIndicator.setVisible(true);
                whiteColorIndicator.setVisible(false);
            }
            else if (this.clientColor == Color.WHITE) {
                whiteColorIndicator.setVisible(true);
                blackColorIndicator.setVisible(false);
            }
        }
        
        private void addPane (int x, int y) {
            Pane pane = new Pane();
            
            pane.setOnMouseClicked(e -> {

                 Thread thread = new Thread(new Runnable(){
            
                    @Override
                    public void run() {
                        if(gamemode == Gamemode.MULTIPLAYER)
                            Cycle.yourTurnCycle(new Point(x, y), clientColor);
                        else if(gamemode == Gamemode.SINGLEPLAYER)
                            Cycle.singleCycle(new Point(x, y), clientColor);
                    }
                });

                thread.setDaemon(true);
                thread.start();

            });
            
            this.grid.add(pane, x, y);
        }
        

    private void setImg(Pane pane, String link) {
        Image image = new Image(Main.class.getResourceAsStream(link));
        ImageView iv = new ImageView(image);
        iv.setFitHeight(30);
        iv.setFitWidth(30);
        pane.getChildren().add(iv);
        pane.getChildren();
    }

    public void deleteStone(Point point) {
        Pane pane = getPaneFromGrid(grid, point.getX(), point.getY());
        pane.getChildren().clear();
     }

    private void addToGrid() {
        for (int i = 0; i < 19; i++){
            for (int j =0; j < 19; j++){
                addPane(i, j);
            }   
        }  
    }

    //Jeśli występuje problem z kładzeniem komienie szukać tutaj x,y -> point
    public void setStoneOnBoard(Point point, Color colorToPlace) {
        Pane pane = getPaneFromGrid(grid, point.getX(), point.getY());

        if(colorToPlace == Color.BLACK) {
            setImg(pane, "/img/blackstonePNG.png");
        }
                
            else if(colorToPlace == Color.WHITE) {
                    setImg(pane, "/img/whitestonePNG.png");
        }
    }

    private Pane getPaneFromGrid(GridPane grid, int x, int y) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                return (Pane) node;
            }
        }
        return null;
    }

    public void disableBoard() {
        for (Node node : grid.getChildren()) {
                node.setDisable(true);
        }
        button_pass.setDisable(true);
    }

    public void enableBoard() {
        for (Node node : grid.getChildren()) {
            node.setDisable(false);
    }
        button_pass.setDisable(false);
    }

    public void disableStartButton() {
        startButton.setDisable(true);
    }
    public void enableStartButton() {
        startButton.setDisable(false);
    }

    public void showTerritoryLabels(int amount) {
        territoryLabel.setVisible(true);
        territoryAmount.setText(Integer.toString(amount));
        territoryAmount.setVisible(true);
    }

    public void showScoreLabels(int amount) {
        scoreLabe.setVisible(true);
        scoreAmount.setText(Integer.toString(amount));
        scoreAmount.setVisible(true);
    }

    @FXML
    void initialize() {
        assert anchorPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert button_shutdown != null : "fx:id=\"button_shutdown\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert grid != null : "fx:id=\"grid\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert button_pass != null : "fx:id=\"button_pass\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert blackColorIndicator != null : "fx:id=\"blackColorIndicator\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert whiteColorIndicator != null : "fx:id=\"whiteColorIndicator\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert captivesAmountLabel != null : "fx:id=\"captivesAmountLabel\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert placedStonesAmountLabel != null : "fx:id=\"placedStonesAmountLabel\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert territoryLabel != null : "fx:id=\"territoryLabel\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert scoreLabe != null : "fx:id=\"scoreLabe\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert territoryAmount != null : "fx:id=\"territoryAmount\" was not injected: check your FXML file 'boardWindow.fxml'.";
        assert scoreAmount != null : "fx:id=\"scoreAmount\" was not injected: check your FXML file 'boardWindow.fxml'.";


        Connector.setBoardControllerInstance(this);
        clientColor = Connector.getClientColor();
        gamemode = Connector.thisgamemode;
        setColorIndicator();
        addToGrid();

        disableBoard();
        disableStartButton();
        setErrorLabel("Wait for another player");


    }
}