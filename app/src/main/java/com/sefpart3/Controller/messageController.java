package com.sefpart3.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.collections.*;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;
import com.sefpart3.Model.*;

public class messageController {
    
    Stage primaryStage;
    Parent root;

    ObservableList<String> HourList = FXCollections.observableArrayList("00", "01", "02","03","04","05","06","07","08","09",
                                                                             "10", "11", "12","13","14","15","16","17","18","19",
                                                                             "20", "21","22","23");
    ObservableList<String> MinuteList = FXCollections.observableArrayList("00","15","30","45");  

    ObservableList<String> nortifyHour = FXCollections.observableArrayList("1","2","3","4","5","6","7","8");  

    @FXML
    private ComboBox<String> startHour;

    @FXML
    private ComboBox<String> startMinute;

    @FXML
    private ComboBox<String> endHour;

    @FXML
    private ComboBox<String> endMinute;

    @FXML
    private ComboBox<String> hourList;

    @FXML
    private TextArea message1;

    @FXML
    private TextArea message2;

    @FXML
    private TextArea message3;

    @FXML
    private Label save_notice;

    @FXML
    private void initialize() {
        Message message = Session.getInstance().getUser().getMessage();
        Schedule schedule = message.getSchedule();

        startHour.setValue(schedule.getStartT().substring(0,2));
        startHour.setItems(HourList);
        startMinute.setValue(schedule.getStartT().substring(3,5));
        startMinute.setItems(MinuteList);
        endHour.setValue(schedule.getEndT().substring(0,2));
        endHour.setItems(HourList);
        endMinute.setValue(schedule.getStartT().substring(3,5));
        endMinute.setItems(MinuteList);
        hourList.setValue(schedule.getFreq());
        hourList.setItems(nortifyHour);

        message1.setText(message.getMessages(0));
        message2.setText(message.getMessages(1));
        message3.setText(message.getMessages(2));
    }

    @FXML
    void save(ActionEvent event) {
        String start_time = startHour.getValue().toString() + ":" + startMinute.getValue().toString();
        String end_time = endHour.getValue().toString() + ":" + endMinute.getValue().toString();
        String frequency = hourList.getValue().toString();
        
        Session.getInstance().getUser().getMessage().addMessage(message1.getText(),0);
        Session.getInstance().getUser().getMessage().addMessage(message2.getText(),1);
        Session.getInstance().getUser().getMessage().addMessage(message3.getText(),2);

        Session.getInstance().getUser().getMessage().setSchedule(start_time, end_time, frequency);

        Session.getInstance().editPerformed("User");
        save_notice.setVisible(true);
    }

    @FXML
    void switchToHomePage(ActionEvent event) throws IOException {
        loadFXML("../View/messageSendView.fxml", event);
    }

    private void loadFXML(String fxmlPath,ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();
    }

}
