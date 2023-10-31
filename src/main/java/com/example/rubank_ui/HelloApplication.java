package com.example.rubank_ui;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application  {
    @Override
    public void start(Stage stage) throws IOException {

        /* Button openButton =new Button("Open");
         openButton.setOnAction(new OpenButtonHandler());*/

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Project 3 - Transaction Manager");
        stage.setScene(scene);
        stage.show();





    }



    public static void main(String[] args) {
        launch();
    }
 class OpenButtonHandler implements EventHandler <ActionEvent>{

     @Override
     public void handle(ActionEvent event) {
         System.out.println("lets go");

     }
 }

}