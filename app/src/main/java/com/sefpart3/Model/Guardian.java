package com.sefpart3.Model;

import javafx.scene.image.Image;
import java.util.*;

public class Guardian {

    private String name = " ";
    private String phoneNo = " ";
    private String WA_Key = " ";
    private String email = " ";
    private Image profilePic;

    public Guardian() {
    }

    public Guardian(String name, String phoneNo, String WA_Key, String email) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.WA_Key = WA_Key;
        this.email = email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setWA_Key(String WA_Key) {
        this.WA_Key = WA_Key;
    }

    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
    
    public String getWA_Key() {
        return WA_Key;
    }

    public Image getProfilePic() {
        return profilePic;
    }

    @Override
    public String toString() {
        //return name + "," + phoneNo + "," + email + "," + twitterID;
        ArrayList<String> temp = new ArrayList<>();

        temp.add(name);
        temp.add(phoneNo);
        temp.add(WA_Key);
        temp.add(email);

        return Database.makeString(temp);
    }

    
}
