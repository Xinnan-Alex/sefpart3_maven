package com.sefpart3.Model;

import java.time.LocalDate;
import java.util.*;

import twitter4j.auth.AccessToken;

public class User extends Account{
    private String name;
    private String phoneNo;

    private String DOB;
    private String address;
    private AccessToken twitter;
    private Guardian guardian = new Guardian();
    private Message message = new Message();
    private CallReminder reminder = new CallReminder();

    public User(String email, String password, String name, String phoneNo){ // new user
        super(email, password, "User");
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public String getName(){ return name;}
    public void setName(String name){ this.name = name; }

    public String getAddress(){ return address; }
    public void setAddress(String address){ this.address = address; }

    public String getDOB(){ return DOB; }
    public void setDOB(String string){ this.DOB = string; }

    public String getPhoneNo(){ return phoneNo; }
    public void setPhoneNo(String phoneNo){ this.phoneNo = phoneNo; }

    public AccessToken getTwitter(){ return twitter; }
    public void setTwitter(String token, String tokensecret){
        this.twitter = new AccessToken(token, tokensecret);
    }

    public void setGuardian(Guardian guardian){ this.guardian = guardian; }
    public Guardian getGuardian(){ return guardian; }

    public Message getMessage(){ return message; }

    public CallReminder getReminder(){ return reminder; }

    @Override
    public void write(boolean exist){
        if(exist)
            Database.writeData("User", toList());
        else
            Database.writeData("User", Arrays.asList(getEmail(), getPassword(), name, phoneNo, " ", " ", guardian.toString(), message.toString(), reminder.toString(), accessTokenToString()));
    }

    @Override
    public List<String> toList(){
        return Arrays.asList(getEmail(), getPassword(), name, phoneNo, 
                             DOB, address, guardian.toString(), message.toString(), 
                             reminder.toString(), accessTokenToString());
    }

    private String accessTokenToString(){
        ArrayList<String> temp = new ArrayList<>();
        temp.add(twitter.getToken());
        temp.add(twitter.getTokenSecret());

        return Database.makeString(temp);
    }

}
