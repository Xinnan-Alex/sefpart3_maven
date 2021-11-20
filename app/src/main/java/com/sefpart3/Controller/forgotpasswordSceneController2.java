package com.sefpart3.Controller;

import java.io.IOException;

import com.sefpart3.Model.Session;
import com.sefpart3.Model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class forgotpasswordSceneController2 {

    @FXML
    TextField oldpasswordField,newpasswordField;

    @FXML
    Button changepasswordButton,backButton;

    private User user;

    public void changepasswordButtonHandler() throws IOException{


        if(user.getPassword().equals(oldpasswordField.getText())){
            changepasword(newpasswordField.getText());
            backButtonHandler();
            new Alert(AlertType.INFORMATION,"Password changed successfully!").showAndWait();
        }else{
            new Alert(AlertType.ERROR,"Invalid old password, please try again!").showAndWait();
        }
    }

    public void changepasword(String password){
        user.setPassword(password);
        Session.getInstance().editPerformed("User");
    }

    public void backButtonHandler() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/loginScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void initialiseUser(User u){
        user = u;
    }
    
}
