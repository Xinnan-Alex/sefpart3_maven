package com.sefpart3.Controller;

import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.sefpart3.Model.GmailAPI;
import com.sefpart3.Model.Session;
import com.sefpart3.Model.User;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class forgotpasswordSceneController implements Initializable{

    @FXML
    TextField emailField;

    @FXML
    Button confirmEmailButton,backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));
        
    }

    public Boolean emailValidation(String email){
        Boolean emailValid = false;

        Session session = Session.getInstance();
        ArrayList<User> users = session.getUsers();

        final Pattern rfc2822 = Pattern.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

        if (!rfc2822.matcher(email).matches()) {
            new Alert(AlertType.ERROR,"Invalid email format, please try again").show();
            emailValid = false;
        }else{
            for(User u:users){
                if (u.getEmail().equals(email)){
                    emailValid = true;
                    break;
                }
                else{
                    new Alert(AlertType.ERROR,"Email is not registerred, please try again").show();
                    emailValid = false;
                }
            }
            ;
        }

        return emailValid;
        
    }
    
    public void confirmEmailButtonHandler() throws Exception{

        String email = emailField.getText();

        if (email == null){
            new Alert(AlertType.ERROR,"Email field is blank, please try again!").showAndWait();
        }else{
            if(emailValidation(email)){
                //process verification process
                long t= System.currentTimeMillis();
                long end = t+300000;
                //generate 5 digit validation code
                SecureRandom random = new SecureRandom();
                int num = random.nextInt(100000);
                String verificationCode = String.format("%05d", num); 
    
                //send email for verification code
                GmailAPI.sendVerificationCode(email, verificationCode);

                //dialog for user to input verification code
                TextInputDialog dialog = new TextInputDialog();
                dialog.setHeaderText("An email contain the verification code has been sent! \nPlease key in your verification code below");
                PauseTransition delay = new PauseTransition(Duration.seconds(300));   //1 minute timer
                delay.setOnFinished(event -> {
                    dialog.close();
                    new Alert(AlertType.ERROR,"Session expired, please register again").show();
                    });
                    
                delay.play();
                while(System.currentTimeMillis() < end) {
                    dialog.showAndWait();
                    String input_verificationcode = dialog.getEditor().getText();
                    if(input_verificationcode.equals(verificationCode)){
                        //create user object with valid information and write into csv file
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/forgotpasswordScene2.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) confirmEmailButton.getScene().getWindow();
                        stage.setScene(new Scene(root));

                    }else{
                        new Alert(AlertType.ERROR,"Invalid verification code, please try again").show();
                    }

                }
            }   
        }
    }

    public void backButtonHandler() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/loginScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    
}
