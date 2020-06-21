package com.tearsvalley.controllers;

import java.io.IOException;

import com.tearsvalley.Connector;
import com.tearsvalley.data.Color;
import com.tearsvalley.data.Gamemode;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class MenuWindowController {

    Color clientColor = Connector.getClientColor();

    @FXML
    private AnchorPane anchor_options_bar;

    @FXML
    private ImageView button_single;

    @FXML
    private ImageView button_multi;

    @FXML
    private ImageView button_home;

    @FXML
    private ImageView button_authors;

    @FXML
    private AnchorPane anchor_authors;

    @FXML
    private AnchorPane anchor_multiplayer;

    @FXML
    private Button buttonMulti_anchorMulti;

    @FXML
    private AnchorPane anchor_home;

    @FXML
    private Button buttonExit_anchorHome;

    @FXML
    private AnchorPane anchor_single;

    @FXML
    private Button buttonSingle_anchorSingle;

    @FXML
    void authorsButtonClicked(MouseEvent event) {
        anchor_single.setVisible(false);
        anchor_multiplayer.setVisible(false);
        anchor_home.setVisible(false);
        anchor_authors.setVisible(true);
    }

    @FXML
    void exitButtonClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void homeButtonClicked(MouseEvent event) {
        anchor_single.setVisible(false);
        anchor_multiplayer.setVisible(false);
        anchor_home.setVisible(true);
        anchor_authors.setVisible(false);
    }

    @FXML
    void multiButtonClicked(MouseEvent event) {
        anchor_single.setVisible(false);
        anchor_multiplayer.setVisible(true);
        anchor_home.setVisible(false);
        anchor_authors.setVisible(false);
    }

    @FXML
    void multiplayerButtonClicked(ActionEvent event) throws IOException{

        Connector.sendGamemode(Gamemode.MULTIPLAYER);


        Parent anotherScene = FXMLLoader.load(getClass().getResource("/fxml/boardWindow.fxml"));
        Scene second = new Scene(anotherScene);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(second);
        window.show();

        Thread thread = new Thread(new Runnable(){
            
            @Override
            public void run() {
                
                    Connector.waitForAnotherPlayer();
            }
        });

        thread.setDaemon(true);
        thread.start();

        }

    @FXML
    void singleButtonClicked(MouseEvent event) {
        anchor_single.setVisible(true);
        anchor_multiplayer.setVisible(false);
        anchor_home.setVisible(false);
        anchor_authors.setVisible(false);
    }

    @FXML
    void singleplayerButtonClicked(ActionEvent event) throws IOException{

        Connector.sendGamemode(Gamemode.SINGLEPLAYER);

        Parent anotherScene = FXMLLoader.load(getClass().getResource("/fxml/boardWindow.fxml"));
        Scene second = new Scene(anotherScene);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(second);
        window.show();

        Thread thread = new Thread(new Runnable(){
            
            @Override
            public void run() {
                
                    Connector.waitForAnotherPlayer();
            }
        });

        thread.setDaemon(true);
        thread.start();

    }

    @FXML
    void initialize() {
        assert anchor_options_bar != null : "fx:id=\"anchor_options_bar\" was not injected: check your FXML file 'firstWindow.fxml'.";
        assert button_single != null : "fx:id=\"button_single\" was not injected: check your FXML file 'firstWindow.fxml'.";
        assert button_multi != null : "fx:id=\"button_multi\" was not injected: check your FXML file 'firstWindow.fxml'.";
        assert button_home != null : "fx:id=\"button_home\" was not injected: check your FXML file 'firstWindow.fxml'.";
        assert button_authors != null : "fx:id=\"button_authors\" was not injected: check your FXML file 'firstWindow.fxml'.";
        assert anchor_authors != null : "fx:id=\"anchor_authors\" was not injected: check your FXML file 'firstWindow.fxml'.";
        assert anchor_multiplayer != null : "fx:id=\"anchor_multiplayer\" was not injected: check your FXML file 'firstWindow.fxml'.";
        assert buttonMulti_anchorMulti != null : "fx:id=\"buttonMulti_anchorMulti\" was not injected: check your FXML file 'firstWindow.fxml'.";
        assert anchor_home != null : "fx:id=\"anchor_home\" was not injected: check your FXML file 'firstWindow.fxml'.";
        assert buttonExit_anchorHome != null : "fx:id=\"buttonExit_anchorHome\" was not injected: check your FXML file 'firstWindow.fxml'.";
        assert anchor_single != null : "fx:id=\"anchor_single\" was not injected: check your FXML file 'firstWindow.fxml'.";
        assert buttonSingle_anchorSingle != null : "fx:id=\"buttonSingle_anchorSingle\" was not injected: check your FXML file 'firstWindow.fxml'.";
    
        if(clientColor == Color.WHITE) {
            button_authors.setVisible(false);
            button_single.setVisible(false);
        }
    }

}
