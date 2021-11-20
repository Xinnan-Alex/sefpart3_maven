package com.sefpart3.Controller;

import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import com.sefpart3.Model.Counsellor;
import com.sefpart3.Model.Session;
import javafx.scene.Node;
import java.io.IOException;


public class CounsellorListController {

    @FXML
    private ArrayList<Button> bookButtons; // to book a counselling session
    @FXML
    private ArrayList<Button> viewButtons; // to view counsellors' profile
    @FXML
    private ArrayList<Label> descLabels; // descriptions for counsellors
    @FXML
    private ArrayList<Label> nameLabels; // counsellors' name

    private ArrayList<Counsellor> counsellors;
    private Stage primaryStage;
    private Parent root;

    public void initialize(){
        counsellors = Session.getInstance().getCounsellors();
        for(int i = 0; i < 5; i++){
            nameLabels.get(i).setText(counsellors.get(i).getName());
            descLabels.get(i).setText(counsellors.get(i).getDesc());
        }
    }

    @FXML
    void switchToBook(ActionEvent event) throws IOException{
        Button temp = (Button)event.getSource();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/RequestCounsellingScene.fxml"));
        root = loader.load();

        RequestCounsellingController rcc = loader.getController();
        
        for(int i = 0; i < 5; i++){
            if(temp.equals(bookButtons.get(i))){
                rcc.setCounsellor(counsellors.get(i));
                break;
            }
        }

        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();
    }

    @FXML
    void switchToHomepage(ActionEvent event) throws IOException{
        loadFXML("/View/userHomepageScene.fxml", event);
    }

    @FXML
    void switchToView(ActionEvent event) throws IOException{
        /*
        Button temp = (Button)event.getSource();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/counsellorProfileScene.fxml"));
        root = loader.load();

        counsellorProfileSceneController cpsc = loader.getController();
        
        for(int i = 0; i < 5; i++){
            if(temp.equals(bookButtons.get(i))){
                cpsc.setCounsellor(counsellors.get(i));
                break;
            }
        }

        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();
        */
    }

    private void loadFXML(String fxmlPath, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 330, 600));
        primaryStage.show();
    }

}
