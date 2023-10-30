package com.example.rubank_ui;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application implements EventHandler<ActionEvent> {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Project 3 - Transaction Manager");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void handle(ActionEvent event){

        if (event.getSource()==open){
            System.out.print("Lets go");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}