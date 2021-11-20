package com.sefpart3.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.*;
import java.net.URI;
import java.net.URISyntaxException;

import com.sefpart3.Model.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.*;
import java.io.IOException;

public class messageSendController {

    Stage primaryStage;
    Parent root;

    String message1 = Session.getInstance().getUser().getMessage().getMessages(0);
    String message2 = Session.getInstance().getUser().getMessage().getMessages(1);
    String message3 = Session.getInstance().getUser().getMessage().getMessages(2);
    ObservableList<String> messages = FXCollections.observableArrayList(message1,message2,message3);

    @FXML
    private ComboBox<String> messageList;

    @FXML
    private void initialize() {
        messageList.setValue(message1);
        messageList.setItems(messages);
    } 

    @FXML
    void send(ActionEvent event) throws Exception {
        // WhatsApp
        String phone_number = Session.getInstance().getUser().getGuardian().getPhoneNo();
        String apiKey = Session.getInstance().getUser().getGuardian().getWA_Key();
        String msg = formatMessage(messageList.getValue().toString());
        String url = "https://api.callmebot.com/whatsapp.php?phone=" + phone_number + "&text=" + msg + "&apikey=" + apiKey;

        try{
            open(url);
        }catch(URISyntaxException e){
            whatsAppAlert();
        }
        

        //Twitter
        
    }

    private String formatMessage(String message){
        char symbol = '+';
        String msg = message.replace(' ',symbol) + "--Sent from: " + Session.getInstance().getUser().getName();

        return msg;
    }

    @FXML
    void switchToMessage(ActionEvent event) throws IOException{
        loadFXML("../View/messageView.fxml",event);
    }

    @FXML
    void switchToHomePage(ActionEvent event) throws IOException{
        loadFXML("../View/userHomepageScene.fxml", event);
    }

    public static void open(String url) throws Exception{
        URI link = new URI(url);
        java.awt.Desktop.getDesktop().browse(link);
    }

    private void whatsAppAlert() throws Exception {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("WhatsApp API key needed");
        alert.setHeaderText("IMPORTANT: Message failed");
        alert.setContentText("You haven't set up guardian WhatsApp key!!");
        alert.showAndWait();
    }

    private void twitterAlert() throws Exception {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Authentication alert");
        alert.setHeaderText("IMPORTANT");
        alert.setContentText("Please connect your Twitter account in profile!");
        alert.showAndWait();
    }

    private void loadFXML(String fxmlPath,ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();
    }

}
