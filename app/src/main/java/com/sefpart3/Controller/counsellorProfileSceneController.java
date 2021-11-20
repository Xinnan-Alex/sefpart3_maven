package com.sefpart3.Controller;

//JAVA IMPORTS
import java.io.IOException;

//JAVAFX IMPORTS
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class counsellorProfileSceneController {

    @FXML
    private Button backButton;

    public void backButtonHandler() throws IOException{
            
        FXMLLoader loader = new FXMLLoader();
        loader = new FXMLLoader(getClass().getResource("../View/counsellorHomepageScene.fxml"));
        Parent root = loader.load();

        // tenantHomepageSceneController controller =  loader.getController();
        // controller.initUserObejct(loggedinPerson);
        
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}