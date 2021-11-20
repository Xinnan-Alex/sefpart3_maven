package com.sefpart3.Controller;

//JAVA IMPORTS
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


import org.json.JSONException;

import com.sefpart3.Model.Session;
import com.sefpart3.Model.TwitterAPI;
import com.sefpart3.Model.User;

import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.Scene;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


public class userProfileSceneController implements Initializable{

    @FXML
    private Button backButton,twitterButton, editProfileButton;

    @FXML
    private TextField userName, userEmail, userAddress, userContactNumber;

    @FXML
    private DatePicker userDob;

    private User user;

    Twitter twitter;
    RequestToken requestToken;
    AccessToken accessToken;
    Boolean setupcomplete = false;

    public void backButtonHandler() throws IOException{
            
        FXMLLoader loader = new FXMLLoader();
        loader = new FXMLLoader(getClass().getResource("../View/userHomepageScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void editProfileHandler(ActionEvent event) throws IOException{
        

        if(editProfileButton.getText().equals("Edit Profile")){
            editProfileButton.setText("Save");
            userName.setEditable(true);
            userDob.setDisable(false);
            userEmail.setEditable(true);
            userAddress.setEditable(true);
            userContactNumber.setEditable(true);
        }
        else{
            editProfileButton.setText("Edit Profile");
            userName.setEditable(false);
            userDob.setDisable(true);
            userEmail.setEditable(false);
            userAddress.setEditable(false);
            userContactNumber.setEditable(false);
            user.setName(userName.getText());
            user.setDOB(userDob.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyy")));
            user.setAddress(userAddress.getText());
            user.setEmail(userEmail.getText());
            user.setPhoneNo(userContactNumber.getText());

            // if(phonenumberValidation(userContactNumber.getText())){    
            //     user.setPhoneNo(userContactNumber.getText());
            // }
            
            Session.getInstance().editPerformed("User");
        }
    }

    public void twitterButtonHandler() throws JSONException, TwitterException{
        
        twitter = TwitterAPI.getTwitterInstance();
        
        // twitter.setOAuthConsumer(twitterSecret.getString("apikey"), twitterSecret.getString("apisecret"));
        requestToken = twitter.getOAuthRequestToken();
        String url = requestToken.getAuthorizationURL();

        if (accessToken == null){
            try {
                System.out.println("request token:" + requestToken);

                Stage stage = new Stage();
                WebView root = new WebView();
                WebEngine engine = root.getEngine();
                
                // fetch the auth URL (in a WebView, to let the user login and then grant authorization to the app)
                engine.load(url);

                engine.locationProperty().addListener((observable, oldValue, newValue) -> {
                    engine.getLoadWorker().stateProperty().addListener((ob, oldv, newv) -> {
                        if(newv == State.SUCCEEDED){
                            
                            while(!setupcomplete){
                                TextInputDialog setupDialog = new TextInputDialog();
                                setupDialog.setHeaderText("Please input your code below");
                                
                                Button cancel = (Button) setupDialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                                cancel.addEventFilter(ActionEvent.ACTION, event ->{
                                        event.consume();
                                });

                                Button okBUtton = (Button) setupDialog.getDialogPane().lookupButton(ButtonType.OK);
                                okBUtton.addEventFilter(ActionEvent.ACTION, event ->{
                                    if (setupDialog.getEditor().getText().isBlank()){
                                        event.consume();
                                    }
                                    else{
                                        try{
                                            //accessToken that need to be store in User Object
                                            accessToken = twitter.getOAuthAccessToken(requestToken, setupDialog.getEditor().getText());
                                            //gettoken and getttokensecret to store into csv
                                            user.setTwitter(accessToken.getToken(), accessToken.getTokenSecret());
                                            Session.getInstance().editPerformed("User");
                                            setupcomplete = true;
                                            stage.close();

                                        }catch(TwitterException e){
                                            e.printStackTrace();
                                            stage.close();
                                        }
                                    }
                                });

                                setupDialog.showAndWait();
                            }
                        }
                    });


                });

                Scene scene =  new Scene(root);
                stage.setScene(scene);
                stage.showAndWait();

            } catch (Exception e) {
                System.out.print("Exception trying to perform OAuth login:");
                e.printStackTrace();
            }
        }
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        userDob.setConverter(new StringConverter<LocalDate>()
    {
        private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

        @Override
        public String toString(LocalDate localDate)
        {
            if(localDate==null)
                return "";
            return dateTimeFormatter.format(localDate);
        }

        @Override
        public LocalDate fromString(String dateString)
        {
            if(dateString==null || dateString.trim().isEmpty())
            {
                return null;
            }
            return LocalDate.parse(dateString,dateTimeFormatter);
        }
    });

        user = Session.getInstance().getUser();

        userDob.setDisable(true);
        userName.setText(user.getName()); 

        if(user.getDOB().equals("null") ){
 
            userDob.setValue(LocalDate.parse("2018-11-01"));

        }else{
            userDob.setValue(LocalDate.parse(user.getDOB(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        
        userEmail.setText(user.getEmail());
        userAddress.setText(user.getAddress());
        userContactNumber.setText(user.getPhoneNo());

        System.out.println("user.getTwitter().getToken():" + user.getTwitter().getToken());
        System.out.println("user.getTwitter().getTokenSecret():" + user.getTwitter().getTokenSecret());
        if (user.getTwitter().getToken() != null || user.getTwitter().getTokenSecret() != null){
            twitterButton.setDisable(true);
            twitterButton.setText("Connected:" + user.getTwitter().getScreenName());
        }
      
    }

}