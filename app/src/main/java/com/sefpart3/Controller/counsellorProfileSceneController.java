package com.sefpart3.Controller;

//JAVA IMPORTS
import java.io.IOException;
import java.util.ResourceBundle;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.net.URL;

import com.sefpart3.Model.CounsellingSession;
import com.sefpart3.Model.Counsellor;
import com.sefpart3.Model.Session;

import javafx.event.ActionEvent;
//JAVAFX IMPORTS
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.Scene;

public class counsellorProfileSceneController implements Initializable{

    @FXML
    private Button backButton, editProfileButton;

    @FXML
    private TextField counsellorName, counsellorLicenseNo, counsellorDesc, counsellorContactNumber;

    @FXML
    private DatePicker counsellorDob;

    private Counsellor counsellor;
    private CounsellingSession cs;
    private Boolean isForeign;
    private String previousPage;

    public void backButtonHandler() throws IOException{
            
        FXMLLoader loader;
        Parent root;

        if(!(isForeign)){
            loader = new FXMLLoader(getClass().getResource("../View/counsellorHomepageScene.fxml"));
            root = loader.load();
        }
        else{
            if(previousPage.equals("Request")){
                loader = new FXMLLoader(getClass().getResource("../View/RequestCounsellingScene.fxml"));
                root = loader.load();
                RequestCounsellingController rcc = loader.getController();
                rcc.setCounsellor(counsellor);
            }
            else if(previousPage.equals("Join")){
                loader = new FXMLLoader(getClass().getResource("../View/JoinCounsellingScene.fxml"));
                root = loader.load();
                JoinCounsellingController jcc = loader.getController(); 
                jcc.setCSession(cs);
            }
            else{
                loader = new FXMLLoader(getClass().getResource("../View/CounsellorListScene.fxml"));
                root = loader.load();
            }
        }

        // tenantHomepageSceneController controller =  loader.getController();
        // controller.initUserObejct(loggedinPerson);
        
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void editProfileHandler(ActionEvent event) throws IOException{
        
        if(editProfileButton.getText().equals("Edit Profile")){
            editProfileButton.setText("Save");
            counsellorName.setEditable(true);
            counsellorDob.setDisable(false);
            counsellorLicenseNo.setEditable(true);
            counsellorDesc.setEditable(true);
            counsellorContactNumber.setEditable(true);
        }
        else{
            editProfileButton.setText("Edit Profile");
            counsellorName.setEditable(false);
            counsellorDob.setDisable(true);
            counsellorLicenseNo.setEditable(false);
            counsellorDesc.setEditable(false);
            counsellorContactNumber.setEditable(false);
            counsellor.setName(counsellorName.getText());
            counsellor.setDOB(counsellorDob.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyy")));
            counsellor.setLicenseNo(counsellorLicenseNo.getText());
            counsellor.setDesc(counsellorDesc.getText());
            counsellor.setPhoneNo(counsellorContactNumber.getText());

            // if(phonenumberValidation(userContactNumber.getText())){    
            //     user.setPhoneNo(userContactNumber.getText());
            // }
            
            Session.getInstance().editPerformed("Counsellor");;
        }
    }

    public void viewProfile(){
        isForeign = false;
        counsellor = Session.getInstance().getCounsellor();

        counsellorDob.setDisable(true);
        counsellorName.setText(counsellor.getName());  

        if(counsellor.getDOB().equals("null") ){
 
            counsellorDob.setValue(LocalDate.parse("2018-11-01"));

        }else{
            counsellorDob.setValue(LocalDate.parse(counsellor.getDOB(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        
        counsellorLicenseNo.setText(counsellor.getLicenseNo());
        counsellorDesc.setText(counsellor.getDesc());
        counsellorContactNumber.setText(counsellor.getPhoneNo());
    }

    public void viewProfile(Counsellor counsellor, String previousPage){
        isForeign = true;
        this.previousPage = previousPage;
        this.counsellor = counsellor;

        counsellorDob.setDisable(true);
        counsellorName.setText(counsellor.getName());  

        if(counsellor.getDOB().equals("null") ){
 
            counsellorDob.setValue(LocalDate.parse("2018-11-01"));

        }else{
            counsellorDob.setValue(LocalDate.parse(counsellor.getDOB(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        counsellorLicenseNo.setText(counsellor.getLicenseNo());
        counsellorDesc.setText(counsellor.getDesc());
        counsellorContactNumber.setText(counsellor.getPhoneNo());

        editProfileButton.setVisible(false);
    }

    public void setCSession(CounsellingSession cs){
        this.cs = cs;
        viewProfile(cs.getCounsellor(), "Join");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        counsellorDob.setConverter(new StringConverter<LocalDate>()
    {
        private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

        @Override
        public String toString(LocalDate localDate)
        {
            if(localDate==null)
                return "";
            return dateTimeFormatter.format(localDate);
        }

        @Override
        public LocalDate fromString(String dateString)
        {
            if(dateString==null || dateString.trim().isEmpty())
            {
                return null;
            }
            return LocalDate.parse(dateString,dateTimeFormatter);
        }
    });


    /*
        counsellor = Session.getInstance().getCounsellor();

        counsellorDob.setDisable(true);
        counsellorName.setText(counsellor.getName());  

        if(counsellor.getDOB().equals("null") ){
 
            counsellorDob.setValue(LocalDate.parse("2018-11-01"));

        }else{
            counsellorDob.setValue(LocalDate.parse(counsellor.getDOB(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        
        counsellorLicenseNo.setText(counsellor.getLicenseNo());
        counsellorDesc.setText(counsellor.getDesc());
        counsellorContactNumber.setText(counsellor.getPhoneNo());
        */
      
    }
}