package com.sefpart3.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.sefpart3.Model.Session;
import com.sefpart3.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class registerGoogleSceneController implements Initializable{

    @FXML
    TextField fullnameField,contactnumField,emailField,passwordField,reenterpasswordField;

    @FXML
    Button googleRegisterButton,registerButton,backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void passInGoogleInfo(String email, String name){
        fullnameField.setText(name);
        emailField.setText(email);

    }

    public void registerButtonHandler() throws Exception{
        Boolean phonenumberValidation = phonenumberValidation(contactnumField.getText());
        Boolean emailValidation = emailValidation(emailField.getText());
        Boolean passwordValidation = passwordValidation(passwordField.getText(), reenterpasswordField.getText());
        Boolean fullnameValidation = fullnameValidation(fullnameField.getText());

        //Check validate input information
        if (phonenumberValidation&&emailValidation&&passwordValidation&&fullnameValidation){
            //create user object with valid information and write into csv file
            User user = new User(emailField.getText(), passwordField.getText(), fullnameField.getText(),contactnumField.getText());
            user.write(false);
            Session.getInstance().addUser(user);
            new Alert(AlertType.CONFIRMATION,"Account created successfully!").showAndWait();
            backButtonHandler();

        }else{
            new Alert(AlertType.ERROR,"Informations are incorrect").show();
        }

    }

    public void backButtonHandler() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/loginScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public Boolean phonenumberValidation(String phonenumber){
        Boolean phonenumberValid = false;

        if (phonenumber.isEmpty()){
            phonenumberValid = false;
        }
        else{
            if ((phonenumber.charAt(0)) == ('0') && (phonenumber.charAt(1)) == ('1')){
                if (phonenumber.length() == 10 || phonenumber.length() == 11){
                    phonenumberValid = true;
                }
                else{
                    phonenumberValid = false;
                }
            }
            else{
                phonenumberValid = false;
            }
        }

        //alert message
        if (!phonenumberValid){
            new Alert(AlertType.ERROR,"Invalid phonenumber, please try again").show();
        }

        return phonenumberValid;

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
                    new Alert(AlertType.ERROR,"Email is already registerred, please try again").show();
                    emailValid = false;
                    break;
                }
                else{
                    emailValid = true;
                }
            }
            ;
        }

        return emailValid;
        
    }

    public Boolean passwordValidation(String password,String reenterpass){
        Boolean passwordValid =  false;

        if(password.isEmpty() || reenterpass.isEmpty()){
            new Alert(AlertType.ERROR,"Password cannot be blank, please try again").show();
            passwordValid = false;
        }else{
            if (password.equals(reenterpass)){
                passwordValid = true;
            }else{
                new Alert(AlertType.ERROR,"Password mistach, please try again").show();
                passwordValid = false;
                
            }
        }

        return passwordValid;
    }

    public Boolean fullnameValidation(String fullname){
        Boolean fullnameValid = false;

        if (fullname.isEmpty()){
            fullnameValid = false;
        }else{
            fullnameValid = true;
        }

        //alert message
        if (!fullnameValid){
            new Alert(AlertType.ERROR,"Invalid full name, please try again").show();
        }

        return fullnameValid;
    }

}
