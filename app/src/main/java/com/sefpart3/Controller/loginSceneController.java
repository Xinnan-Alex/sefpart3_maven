package com.sefpart3.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

import com.sefpart3.Model.Counsellor;
import com.sefpart3.Model.Google;
import com.sefpart3.Model.OAuthAuthenticator;
import com.sefpart3.Model.OAuthCompletedCallback;
import com.sefpart3.Model.OAuthGoogleAuthenticator;
import com.sefpart3.Model.Session;
import com.sefpart3.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class loginSceneController implements Initializable,OAuthCompletedCallback{

    @FXML
    TextField emailField;

    @FXML
    PasswordField passwordField;

    @FXML
    Hyperlink forgotPasswordhHyperlink,signupHyperlink;

    @FXML
    Button signinButton, googleSigninButton;

    JSONObject googledata;
    Google google =  new Google();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));

        passwordField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));
        
    }

    public void signinButtonHandler() throws IOException{
        Session session = Session.getInstance();
        ArrayList<User> userList = session.getUsers();
        ArrayList<Counsellor> counsellorList = session.getCounsellors();
       
        for (User u:userList){
            if (u.getEmail().equals(emailField.getText())){
                if (u.getPassword().equals(passwordField.getText())){
                    session.setLoginStatus(true);
                    session.setUser(u);
                    break;
                }
            }
        }

        for (Counsellor c:counsellorList){
            if (c.getEmail().equals(emailField.getText())){
                if (c.getPassword().equals(passwordField.getText())){
                    session.setLoginStatus(true);
                    session.setUser(c);
                    break;
                }
            }
        }

        if(session.getLoginStatus()){
            //move to homepage scene
            FXMLLoader loader;
            if(session.getRole().equals("User"))
                loader = new FXMLLoader(getClass().getResource("../View/userHomepageScene.fxml"));
            else
                loader = new FXMLLoader(getClass().getResource("../View/counsellorHomepageScene.fxml"));
                
            Parent root = loader.load();

            Stage stage = (Stage) googleSigninButton.getScene().getWindow();
            stage.setScene(new Scene(root));


        }else{
            new Alert(AlertType.ERROR,"Error: Invalid login details, please try again! ").show();
        }

    }

    public void signupHyperlinkHandler() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/registerScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) signupHyperlink.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void googleSigninButtonHandler() throws JSONException, IOException{

        final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        String filepath =  "../Resources/credentials.json";
        String gRedir = "http://localhost";
        String gScope = "profile email";
        
        InputStream in = registerSceneController.class.getResourceAsStream(filepath);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        OAuthAuthenticator auth = new OAuthGoogleAuthenticator(clientSecrets.getDetails().getClientId().toString(), gRedir, clientSecrets.getDetails().getClientSecret(), gScope);
                                                                
        auth.startLogin();
        oAuthCallback(auth);

        // if google login recognise email in system, and google login is successful, user will proceed to be logged in, no password require.
        if(Session.getInstance().getLoginStatus()){
            //move to homepage scene

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/userHomepageScene.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) googleSigninButton.getScene().getWindow();
            stage.setScene(new Scene(root));


        }else{
            new Alert(AlertType.ERROR,"Error: Invalid login details, please try again! ").show();
        }

    }

    public void forgotPasswordhHyperlinkHandler(){
        
        


    }

    @Override
    public void oAuthCallback(OAuthAuthenticator authenticator){
        try{
            googledata = authenticator.getJsonData();
            ArrayList<User> userList = Session.getInstance().getUsers();
            ArrayList<Counsellor> counsellorList = Session.getInstance().getCounsellors();

            //Checking if the the person trying to login is a user or counsellor
            for (User u:userList){
                if (u.getEmail().equals(googledata.getString("email"))){
                    Session.getInstance().setLoginStatus(true);
                    Session.getInstance().setUser(u);
                    break;
                }
                else{
                    for (Counsellor c:counsellorList){
                        if (c.getEmail().equals(googledata.getString("email"))){
                            Session.getInstance().setLoginStatus(true);
                            Session.getInstance().setUser(c);
                            break;
                        }
                        else{
                            Session.getInstance().setLoginStatus(false);
                        }
                        Session.getInstance().setLoginStatus(false);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
            

    }

}
