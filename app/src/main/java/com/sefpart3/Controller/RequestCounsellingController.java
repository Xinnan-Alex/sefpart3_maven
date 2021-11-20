package com.sefpart3.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import com.sefpart3.Model.CounsellingSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import com.sefpart3.Model.Counsellor;
import com.sefpart3.Model.Session;
import javafx.scene.Node;
import java.io.IOException;

public class RequestCounsellingController {

    ObservableList<String> HourList = FXCollections.observableArrayList("00", "01", "02","03","04","05","06","07","08","09",
                                                                             "10", "11", "12","13","14","15","16","17","18","19",
                                                                             "20", "21","22","23");

    ObservableList<String> MinuteList = FXCollections.observableArrayList("00","15","30","45");    
    
    @FXML
    private DatePicker date;

    @FXML
    private Button name;

    @FXML
    private TextField reason;

    @FXML
    private ComboBox<String> time_hour;

    @FXML
    private ComboBox<String> time_minute;

    private Counsellor counsellor;
    private Stage primaryStage;
    private Parent root;

    @FXML
    private void initialize() {
        time_hour.setValue("00");
        time_hour.setItems(HourList);
        time_minute.setValue("00");
        time_minute.setItems(MinuteList);
    }
    
    @FXML
    void switchToCList(ActionEvent event) throws IOException {
        loadFXML("../View/CounsellorListScene.fxml", event);
    }

    @FXML
    void switchToHomepage(ActionEvent event) throws IOException{
        LocalDate selectedDate = date.getValue();
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dft.format(selectedDate);

        String time = time_hour.getValue().toString() + ":" + time_minute.getValue().toString();

        String link = "https://meet.google.com/new";

        CounsellingSession cs = new CounsellingSession(Session.getInstance().getUser() , counsellor, selectedDate.toString() , reason.getText(), time, link);
        cs.write();
        Session.getInstance().addCSession(cs);
        loadFXML("../View/userHomepageScene.fxml", event);
    }

    @FXML
    void switchToView(ActionEvent event) throws IOException{
        /*
        Button temp = (Button)event.getSource();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/counsellorProfileScene.fxml"));
        root = loader.load();

        counsellorProfileSceneController cpsc = loader.getController();
        
        cpsc.setCounsellor(counsellor);

        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();
        //*/
    }

    public void setCounsellor(Counsellor counsellor){
        this.counsellor = counsellor;
        name.setText(counsellor.getName());
    }

    private void loadFXML(String fxmlPath, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();
    }
}
