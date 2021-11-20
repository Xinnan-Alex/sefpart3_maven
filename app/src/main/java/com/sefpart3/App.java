package com.sefpart3;

import java.io.IOException;

import com.sefpart3.Model.TwitterAPI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javafx.scene.Parent;

public class App extends Application{

    private Scene scene;
    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("View/loginScene.fxml"));
        scene = new Scene(root);

        primaryStage.setScene(scene);

        Image stageicon = new Image(getClass().getResource("Resources/images/iamfine.png").toURI().toString());

         //adding icon to the window
        primaryStage.getIcons().add(stageicon);
         //adding a title to the window
        primaryStage.setTitle("I Am Fine Notification System");

        primaryStage.setResizable(false);
        primaryStage.show();

        //TwitterAPI.sendTweet(accessToken, tweetmessage);

    }

    public static void main(String[] args) throws IOException {
        launch(args);
        
    }
}
