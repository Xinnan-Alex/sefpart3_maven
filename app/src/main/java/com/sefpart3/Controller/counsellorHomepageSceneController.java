package com.sefpart3.Controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.sefpart3.Model.Session;

public class counsellorHomepageSceneController {

    @FXML
    private Button counsellorProfileBtn, counsellorManageCounsellingSessionBtn, logoutButton ;

    public void counsellorProfileBtnHandler() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/counsellorProfileScene.fxml"));
        Parent root = fxmlLoader.load();
        
        //userProfileSceneController controller = fxmlLoader.getController();
        //controller.initUserObejct(loggedinPerson);
        
        Stage window = (Stage)counsellorProfileBtn.getScene().getWindow();
        window.setScene(new Scene(root)); 
    }

    public void counsellorManageCounsellingSessionBtnHandler() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/ManageCounsellingScene.fxml"));
        Parent root = fxmlLoader.load();
        
        //userProfileSceneController controller = fxmlLoader.getController();
        //controller.initUserObejct(loggedinPerson);
        
        Stage window = (Stage)counsellorManageCounsellingSessionBtn.getScene().getWindow();
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
    
}
