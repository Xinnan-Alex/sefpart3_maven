package com.sefpart3.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.sefpart3.Model.Google;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class testhomepageController implements Initializable{
    
    @FXML
    Label nameLabel,emailLabel,idLabel;

    Google google;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }

    public void passInGoogleInfo(Google passedinGooogle){
        google = passedinGooogle;
        nameLabel.setText(google.getName());
        emailLabel.setText(google.getEmail());
        idLabel.setText(google.getId());
    }
}