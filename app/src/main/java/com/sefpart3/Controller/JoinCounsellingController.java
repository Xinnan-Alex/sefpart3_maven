package com.sefpart3.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import com.sefpart3.Model.CounsellingSession;
import com.sefpart3.Model.Session;
import javafx.scene.Node;
import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;

public class JoinCounsellingController {

    @FXML
    private Label date;

    @FXML
    private Label reason;

    @FXML
    private Button name;

    @FXML
    private Label time;

    private CounsellingSession cs;
    private String role;
    private Stage primaryStage;
    private Parent root;

    public void initialize(){
        Session session = Session.getInstance();
        role = session.getRole();
        
        if(cs == null)
            cs = session.getPersonalSessions().get(0);

        if(role.equals("Counsellor"))
            name.setText(cs.getUser().getName());
        else // role == User
            name.setText(cs.getCounsellor().getName());

        date.setText(cs.getDate());
        time.setText(cs.getSchedule().getTime());
        reason.setText(cs.getReason());
    
    }   

    @FXML
    void switchToHomepage(ActionEvent event) throws IOException{
        if(role.equals("User"))
            loadFXML("/View/userHomepageScene.fxml", event);
        else
            loadFXML("/View/counsellorHomepageScene.fxml", event);
    }

    @FXML
    void switchToJoin(ActionEvent event) throws IOException{
        try {
            Desktop.getDesktop().browse(new URI(cs.getLink()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        cs.setJoined(role, true);

        if(cs.getJoined("User") && cs.getJoined("Counsellor")) // if both user and counsellor has joined the google meet, 
            Session.getInstance().removeCounsellingSessions(cs); // this counselling session will be removed

        loadFXML("/View/userHomepageScene.fxml", event);
    }

    @FXML
    void switchToView(ActionEvent event) throws IOException{
        /*
        FXMLLoader loader;

        if(role.equals("User")){ // login as user, check counsellor's profile
            loader = new FXMLLoader(getClass().getResource("/View/counsellorProfileScene.fxml"));
            root = loader.load();
            counsellorProfileSceneController cpsc = loader.getController();
        }
        else{ // login as counsellor, check user's profile
            loader = new FXMLLoader(getClass().getResource("/View/userProfileScene.fxml"));
            root = loader.load();
            userProfileSceneController upsc = loader.getController();
        }

        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();
        */
    }

    public void cancelSession(ActionEvent event)throws IOException{
        Session.getInstance().removeCounsellingSessions(cs);

        loadFXML("/View/userHomepageScene.fxml", event);
    }

    public void setCSession(CounsellingSession cs){
        this.cs = cs;
    }

    private void loadFXML(String fxmlPath, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();
    }

}
