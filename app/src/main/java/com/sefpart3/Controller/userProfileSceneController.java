package com.sefpart3.Controller;

import java.io.BufferedReader;
//JAVA IMPORTS
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.accessibility.AccessibleSelection;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import org.apache.http.util.Args;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sefpart3.Model.OAuthAuthenticator;
import com.sefpart3.Model.OAuthCompletedCallback;
import com.sefpart3.Model.OAuthTwitterAuthenticator;
import com.sefpart3.Model.TwitterAPI;
import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import twitter4j.TwitterRuntimeException;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class userProfileSceneController implements Initializable{

    @FXML
    private Button backButton,twitterButton;

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

    public void twitterButtonHandler() throws JSONException, TwitterException{
        
        String filepath =  "../Resources/twitterSecret.json";
        InputStream is = userProfileSceneController.class.getResourceAsStream(filepath);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        JSONTokener tokener = new JSONTokener(in);
        JSONObject twitterSecret = new JSONObject(tokener);
        twitter = new TwitterAPI(twitterSecret.getString("apikey"), twitterSecret.getString("apisecret")).getTwitterInstance();
        
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
                                            System.out.println(accessToken.getToken());
                                            System.out.println(twitter.getConfiguration().getOAuthConsumerKey());
                                            setupcomplete = true;

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
        if (accessToken!=null){
            twitterButton.setDisable(true);
        }
            
    }

}