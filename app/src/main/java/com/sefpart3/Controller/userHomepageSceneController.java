package com.sefpart3.Controller;

//JAVA IMPORTS
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import com.sefpart3.Model.Session;
import com.sefpart3.Model.User;
import com.sefpart3.Model.Schedule;
import javafx.application.Platform;
//JAVAFX IMPORTS
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.media.Media;  
import javafx.scene.media.MediaPlayer;  
import java.io.File;
import java.lang.Thread;

public class userHomepageSceneController implements Initializable{

    Schedule msg = Session.getInstance().getUser().getMessage().getSchedule();
    Schedule call = Session.getInstance().getUser().getReminder().getSchedule();

    @FXML
    private Button userProfileBtn, userCounsellingSessionBtn, userCallReminderBtn, userRequestSessionBtn, userMessageBtn, userConfigureCallReminderBtn, userConfigureMessageBtn, logoutButton ;

    @FXML
    Label nameLabel;

    private User user;


    @Override
    public void initialize(URL location, ResourceBundle resources){
        user = Session.getInstance().getUser();
        nameLabel.setText(user.getName());
        //Display reminder nortification 
        try {
            runInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void userViewProfileBtnHandler() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/userProfileScene.fxml"));
        Parent root = fxmlLoader.load();
        userProfileSceneController controller = fxmlLoader.getController();
        controller.viewProfile();

        //controller.initUserObejct(loggedinPerson);
        
        Stage window = (Stage)userProfileBtn.getScene().getWindow();
        window.setScene(new Scene(root)); 
    }

    public void userCounsellingSessionBtnHandler() throws IOException {
        FXMLLoader fxmlLoader;

        System.out.println("No of personal: " + Session.getInstance().getPersonalSessions().size());

        if(Session.getInstance().getPersonalSessions().size() > 0)
            fxmlLoader = new FXMLLoader(getClass().getResource("../View/JoinCounsellingScene.fxml"));
        else
            fxmlLoader = new FXMLLoader(getClass().getResource("../View/CounsellorListScene.fxml"));

        Parent root = fxmlLoader.load();
        
        //userProfileSceneController controller = fxmlLoader.getController();
        //controller.initUserObejct(loggedinPerson);
        
        Stage window = (Stage)userCounsellingSessionBtn.getScene().getWindow();
        window.setScene(new Scene(root)); 
    }

    public void userCallReminderBtnHandler() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/callView.fxml"));
        Parent root = fxmlLoader.load();
        
        //userProfileSceneController controller = fxmlLoader.getController();
        //controller.initUserObejct(loggedinPerson);
        
        Stage window = (Stage)userCallReminderBtn.getScene().getWindow();
        window.setScene(new Scene(root)); 
    }

    public void userMessageBtnHandler() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/messageSendView.fxml"));
        Parent root = fxmlLoader.load();
        
        //userProfileSceneController controller = fxmlLoader.getController();
        //controller.initUserObejct(loggedinPerson);
        
        Stage window = (Stage)userMessageBtn.getScene().getWindow();
        window.setScene(new Scene(root)); 
    }

    public void logoutButtonHandler() throws IOException{
        Alert confirmation_Alert = new Alert(AlertType.CONFIRMATION,"Do you wish to logout?",ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        confirmation_Alert.showAndWait();

        if (confirmation_Alert.getResult() == ButtonType.YES){
        
            Session.getInstance().setLoginStatus(false);
            Parent root = FXMLLoader.load(getClass().getResource("../View/loginScene.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
    }

    private void runInBackground() throws Exception{
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    displayNortification();
                } catch (Exception e) {
                    e.printStackTrace();
                }                    
            }
        }).start();    
    }

    private void displayNortification() throws Exception{
        for (;;){
            Thread.sleep(1000);
            if(msg.tick()){
                String path = "src/Resources/sound/IamFine.mp3";    
                Media media = new Media(new File(path).toURI().toString());  
        
                MediaPlayer mediaPlayer = new MediaPlayer(media);    
                mediaPlayer.setAutoPlay(true); 
                try {
                    nortification("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.sleep(60000);
            }
            if(call.tick()){
                String path = "src/Resources/sound/IamFine.mp3";    
                Media media = new Media(new File(path).toURI().toString());  
        
                MediaPlayer mediaPlayer = new MediaPlayer(media);    
                mediaPlayer.setAutoPlay(true); 
                try {
                    nortification("call");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.sleep(60000);
            }
        }
    }

    private void nortification(String type) throws Exception{
        String title,headerText,contentText;
        if (type.equals("call")){
            title = "Call reminder";
            headerText = "Let your guardians know whether you are fine!";
            contentText = "Call your guardian now";
        }
        else {
            title = "Sending message reminder";
            headerText = "Let your guardians know whether you are fine!";
            contentText = "Send your message now";
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(headerText);
                alert.setContentText(contentText);
                alert.showAndWait();
            }
        });
    }
}
