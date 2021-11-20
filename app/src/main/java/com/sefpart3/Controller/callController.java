package com.sefpart3.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.*;
import java.net.URI;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import com.sefpart3.Model.*;
public class callController {
    
    Stage primaryStage;
    Parent root;

    ObservableList<String> guardians = FXCollections.observableArrayList(Session.getInstance().getUser().getGuardian().getName()); 

    @FXML
    private ComboBox<String> guardianList;

    @FXML
    private void initialize() {
        guardianList.setValue(Session.getInstance().getUser().getGuardian().getName());
        guardianList.setItems(guardians);
    }

    @FXML
    void call(ActionEvent event) throws Exception{
        String ans = confirmationAlert();

        // Open Google Meet Link
        if (ans.equals("yes")){
            String url = Session.getInstance().getUser().getReminder().getLink();
            open(url);
        }
    }

    @FXML
    void switchToCallReminder(ActionEvent event) throws IOException {
        loadFXML("/View/callReminderView.fxml",event);
    }

    @FXML
    void switchToHomePage(ActionEvent event) throws IOException{
        loadFXML("/View/userHomepageScene.fxml", event);
    }

    private String confirmationAlert() throws Exception {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(" Call confirmation");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Confirm to call this user via Google Meet");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
            return "yes";
        else
            return "no";
    }

    public static void open(String url) throws Exception{
        URI link = new URI(url);
        java.awt.Desktop.getDesktop().browse(link);
    }

    private void loadFXML(String fxmlPath,ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();
    }
}