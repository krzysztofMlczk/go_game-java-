package com.tearsvalley;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StartGraphicMode extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/menuWindow.fxml"));
        stage.initStyle(StageStyle.TRANSPARENT); // powoduje nie wyswietlanie sie gornego paska (minimalizacja, zamknij itd.)
        Scene scene = new Scene(root); // tworzymy nową scenę 
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT); // ustawiamy transparentne wypełnienie
        stage.setScene(scene);  
        stage.show();
    }
}