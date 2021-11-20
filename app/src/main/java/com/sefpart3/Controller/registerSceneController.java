package com.sefpart3.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import org.json.JSONObject;

import com.sefpart3.Model.GmailAPI;
import com.sefpart3.Model.OAuthAuthenticator;
import com.sefpart3.Model.OAuthCompletedCallback;
import com.sefpart3.Model.OAuthGoogleAuthenticator;
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

public class registerSceneController implements Initializable,OAuthCompletedCallback{

    @FXML
    TextField fullnameField,contactnumField,emailField,passwordField,reenterpasswordField;

    @FXML
    Button googleRegisterButton,registerButton,backButton;

    String verificationCode;

    JSONObject googledata;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));

        reenterpasswordField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));

        emailField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));

        contactnumField.setTextFormatter(new TextFormatter<>(c -> {
            if (!c.getControlNewText().matches("\\d*")) 
                return null;
            else
                return c;
            }
        ));
        
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

    public void registerButtonHandler() throws Exception{
        Boolean phonenumberValidation = phonenumberValidation(contactnumField.getText());
        Boolean emailValidation = emailValidation(emailField.getText());
        Boolean passwordValidation = passwordValidation(passwordField.getText(), reenterpasswordField.getText());
        Boolean fullnameValidation = fullnameValidation(fullnameField.getText());

        //Check validate input information
        if (phonenumberValidation&&emailValidation&&passwordValidation&&fullnameValidation){
            long t= System.currentTimeMillis();
            long end = t+300000;
            //generate 5 digit validation code
            SecureRandom random = new SecureRandom();
            int num = random.nextInt(100000);
            verificationCode = String.format("%05d", num); 
  
            //send email for verification code
            GmailAPI.sendVerificationCode(emailField.getText(), verificationCode);

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
                    User user = new User(emailField.getText(), passwordField.getText(), fullnameField.getText(),contactnumField.getText());
                    user.write(false);
                    Session.getInstance().addUser(user);
                    new Alert(AlertType.CONFIRMATION,"Account created successfully!").showAndWait();
                    backButtonHandler();
                    break;
                }else{
                    new Alert(AlertType.ERROR,"Invalid verification code, please try again").show();
                }

            }

        }else{
            new Alert(AlertType.ERROR,"Informations are incorrect").show();
        }


        

    }

    public void googleRegisterButtonHandler() throws Exception{
        final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        String filepath =  "../Resources/credentials.json";
        String gRedir = "http://localhost";
        String gScope = "profile email";
        
        InputStream in = registerSceneController.class.getResourceAsStream(filepath);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        OAuthAuthenticator auth = new OAuthGoogleAuthenticator(clientSecrets.getDetails().getClientId().toString(), 
                                                                gRedir, 
                                                                clientSecrets.getDetails().getClientSecret(), gScope);
        auth.startLogin();
        oAuthCallback(auth);

    }

    @Override
    public void oAuthCallback(OAuthAuthenticator authenticator) throws Exception {
        googledata = authenticator.getJsonData();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/registerGoogleScene.fxml"));
        Parent root = loader.load();

        registerGoogleSceneController controller = loader.getController();
        controller.passInGoogleInfo(googledata.getString("email"),googledata.getString("name"));

        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setScene(new Scene(root));

    }

    public void backButtonHandler() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/loginScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public boolean ContactnumValidation(String contactnum){
        boolean registerContactNumValid = false;

        if (contactnum.length() == 0){
            registerContactNumValid = false;
        }
        else{
            if ((contactnum.charAt(0)) == ('0') && (contactnum.charAt(1)) == ('1')){
                if (contactnum.length() == 10 || contactnum.length() == 11){
                    registerContactNumValid = true;
                }
                else{
                    registerContactNumValid = false;
                }
            }
            else{
                registerContactNumValid = false;
            }
        }

        return registerContactNumValid;
    }

    
}
