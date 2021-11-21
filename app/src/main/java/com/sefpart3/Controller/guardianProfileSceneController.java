package com.sefpart3.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.sefpart3.Model.Guardian;
import com.sefpart3.Model.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class guardianProfileSceneController implements Initializable{
    
    @FXML
    TextField guardianFullnameField,guardianPhoneNumField,guardianEmailField,guardianWAKeyField;

    @FXML
    Button confirmButton,backButton,changePicButton;

    @FXML
    ImageView guardianProfilePic;

    private Guardian guardian;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        guardian = Session.getInstance().getUser().getGuardian();
        guardianFullnameField.setText(guardian.getName());
        guardianPhoneNumField.setText(guardian.getPhoneNo());
        guardianEmailField.setText(guardian.getEmail());
        guardianWAKeyField.setText(guardian.getWA_Key());
    }


    public void confirmButtonHandler(){
        guardian.setName(guardianFullnameField.getText());
        guardian.setPhoneNo(guardianPhoneNumField.getText());
        guardian.setEmail(guardianEmailField.getText());
        guardian.setWA_Key(guardianWAKeyField.getText());
        Session.getInstance().editPerformed("User");
    }

    public void backButtonHandler() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/userProfileScene.fxml"));
        Parent root = fxmlLoader.load();

        Stage window = (Stage)backButton.getScene().getWindow();
        window.setScene(new Scene(root)); 

    }

    public void changePicButtonHandler(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file.getName());

        guardian.setProfilePic(new Image(file.getPath()));
        // guardianProfilePic.setImage(guardian.getProfilePic());

    }


}
