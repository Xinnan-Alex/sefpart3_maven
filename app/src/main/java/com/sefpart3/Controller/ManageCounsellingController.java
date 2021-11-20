package com.sefpart3.Controller;

import java.util.*;

import com.sefpart3.Model.CounsellingSession;
import com.sefpart3.Model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class ManageCounsellingController {

    @FXML
    private ArrayList<Button> joinButtons; // to join a counselling session

    @FXML
    private ArrayList<Label> descLabels; // users' reason

    @FXML
    private ArrayList<Label> nameLabels; // users' name

    private ArrayList<CounsellingSession> counsellingSessions;
    private Stage primaryStage;
    private Parent root;

    public void initialize() {
        counsellingSessions = Session.getInstance().getPersonalSessions();
        for(int i = 0; i < 5; i++){
            try{
            CounsellingSession current = counsellingSessions.get(i);
            nameLabels.get(i).setText(current.getUser().getName());
            descLabels.get(i).setText(counsellingSessions.get(i).getReason());
            }catch(IndexOutOfBoundsException e){
                joinButtons.get(i).setVisible(false);
                descLabels.get(i).setVisible(false);
                nameLabels.get(i).setVisible(false);
            }
        }
    }

    @FXML
    void switchToHomepage(ActionEvent event) throws IOException{
        loadFXML("/View/counsellorHomepageScene.fxml", event);
    }

    @FXML
    void switchToJoin(ActionEvent event) throws IOException{
        Button temp = (Button)event.getSource();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/JoinCounsellingScene.fxml"));
        root = loader.load();

        JoinCounsellingController jcc = loader.getController();
        
        for(int i = 0; i < 5; i++){
            if(temp.equals(joinButtons.get(i))){
                jcc.setCSession(counsellingSessions.get(i));
                break;
            }
        }

        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();

    }

    private void loadFXML(String fxmlPath, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();
    }

}
