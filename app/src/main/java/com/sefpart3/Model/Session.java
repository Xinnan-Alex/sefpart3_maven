package com.sefpart3.Model;

import javafx.stage.Stage;
import java.util.*;

public class Session {
    //Singleton Design Pattern
    private static Session instance;

    private Stage publicStage;
    private boolean isLoggedIn = false; //this becomes true when someone logs in
    private User user;
    private Counsellor counsellor;
    private String role;

    // to load all the accounts
    private ArrayList<User> users = new ArrayList<>(); 
    private ArrayList<Counsellor> counsellors = new ArrayList<>();
    private ArrayList<CounsellingSession> counsellingSessions = new ArrayList<>();

    private Session(){
        init();
    }

    private void init(){ // load everything from csv

        List<List<String>> userDB = Database.readData("User");
        List<List<String>> counsellorDB = Database.readData("Counsellor");
        List<List<String>> counsellingSessionDB = Database.readData("Counselling Session");

        String email;
        String password;
        String name;
        String DOB;
        String phoneNo;
        

        for(int i = 0; i < userDB.size(); i++){
            List<String> guardian;
            List<String> messages;
            List<String> reminder;
            //String twitter;
            String address;

            email = userDB.get(i).get(0);
            password = userDB.get(i).get(1);
            name = userDB.get(i).get(2);
            phoneNo = userDB.get(i).get(3);
            DOB = userDB.get(i).get(4);
            address= userDB.get(i).get(5);
            guardian = Database.parseArray(userDB.get(i).get(6));
            messages = Database.parseArray(userDB.get(i).get(7));
            reminder = Database.parseArray(userDB.get(i).get(8));
            

            users.add(new User(email, password, name, phoneNo));
            users.get(i).setDOB(DOB);
            users.get(i).setAddress(address);
            if(guardian.size() > 1)
                users.get(i).setGuardian(new Guardian(guardian.get(0), guardian.get(1), guardian.get(2), guardian.get(3)));
            if(messages.size() > 1){
                users.get(i).getMessage().setSchedule(messages.get(0), messages.get(1), messages.get(2));
                for(int j = 3; j < 6; j++) users.get(i).getMessage().addMessage(messages.get(j), j - 3);
            }
            if(reminder.size() > 1){
                users.get(i).getReminder().setSchedule(reminder.get(0), reminder.get(1), reminder.get(2));
                users.get(i).getReminder().setLink(reminder.get(3));
            }
        }
        System.out.println("Total no. of User: " + users.size());

        for(int i = 0; i < counsellorDB.size(); i++){
            String licenseNo;
            String desc;

            email = counsellorDB.get(i).get(0);
            password = counsellorDB.get(i).get(1);
            name = counsellorDB.get(i).get(2);
            phoneNo = counsellorDB.get(i).get(3);
            DOB = counsellorDB.get(i).get(4);
            licenseNo = counsellorDB.get(i).get(5);
            desc = counsellorDB.get(i).get(6);

            counsellors.add(new Counsellor(email, password, name, phoneNo, licenseNo));
            counsellors.get(i).setDOB(DOB);
            counsellors.get(i).setDesc(desc);
        }

        System.out.println("Total no. of Counsellor: " + counsellorDB.size());

        for(int i = 0; i < counsellingSessionDB.size(); i++){
            User c_user = null;
            Counsellor c_counsellor = null;

            String user_email = counsellingSessionDB.get(i).get(0);
            String counsellor_email = counsellingSessionDB.get(i).get(1);
            String date = counsellingSessionDB.get(i).get(2);
            String reason = counsellingSessionDB.get(i).get(3);
            String time = counsellingSessionDB.get(i).get(4);
            String link = counsellingSessionDB.get(i).get(5);
            String user_Joined = counsellingSessionDB.get(i).get(6);
            String counsellor_Joined = counsellingSessionDB.get(i).get(7);

            for(int j = 0; j < users.size(); j++){
                if(users.get(j).getEmail().equals(user_email)){
                    c_user = users.get(j);
                    break;
                }
            }

            for(int j = 0; j < counsellors.size(); j++){
                if(counsellors.get(j).getEmail().equals(counsellor_email)){
                    c_counsellor = counsellors.get(j);
                    break;
                }
            }

            counsellingSessions.add(new CounsellingSession(c_user, c_counsellor, date, reason, time, link));
            counsellingSessions.get(i).setJoined("User", Boolean.parseBoolean(user_Joined));
            counsellingSessions.get(i).setJoined("Counsellor", Boolean.parseBoolean(counsellor_Joined));
        }
        System.out.println("Total no. of Counselling Session: " + counsellingSessions.size());

        if(isLoggedIn){
            
        }
    }

    public static Session getInstance(){
        if(instance==null) instance = new Session();

        return instance;
    }

    public void editPerformed(String type){ // it will rewrite the whole csv file according to the type
        ArrayList<List<String>> temp = new ArrayList<>();

        if(type.equals("User"))
            for(int i = 0; i < users.size(); i++)
                temp.add(users.get(i).toList());  

        else if(type.equals("Counsellor"))
            for(int i = 0; i < counsellors.size(); i++)
                temp.add(counsellors.get(i).toList());

        else if(type.equals("Counselling Session"))
            for(int i = 0; i < counsellingSessions.size(); i++)
                temp.add(counsellingSessions.get(i).toList());

        Database.writeAllData(type, temp);
    }


    public void setUser(Counsellor counsellor){
        this.counsellor = counsellor;
        role = "Counsellor";
    }
    public void setUser(User user){
        this.user = user; 
        role = "User";
    }
    public User getUser(){ return user; }
    public Counsellor getCounsellor(){ return counsellor; }

    public String getRole(){ return role; }

    public ArrayList<CounsellingSession> getPersonalSessions(){
        ArrayList<CounsellingSession> temp = new ArrayList<>();

        if(role.equals("Counsellor")){
            for(int i = 0; i < counsellingSessions.size(); i++){
                if(counsellor.equals(counsellingSessions.get(i).getCounsellor()))
                    temp.add(counsellingSessions.get(i));
            }
        }
        else{ // role == "User"
            for(int i = 0; i < counsellingSessions.size(); i++){
                if(user.equals(counsellingSessions.get(i).getUser())){
                    temp.add(counsellingSessions.get(i));
                    break;
                }
            }
        }
        return temp;
    }

    public void removeCounsellingSessions(CounsellingSession cs){
        counsellingSessions.remove(cs);
        editPerformed("Counselling Session");
    }

    public void setStage(Stage s){ this.publicStage = s;}
    public Stage getStage(){ return publicStage; }

    public ArrayList<User> getUsers(){
        return users;
    }

    public ArrayList<Counsellor> getCounsellors(){
        return counsellors;
    }

    public void setLoginStatus(boolean isLoggedIn){ this.isLoggedIn = isLoggedIn; }
    public boolean getLoginStatus(){ return isLoggedIn; }

    public void addUser(User u){
        users.add(u);
    }

    public void addUser(Counsellor c){
        counsellors.add(c);
    }

    public void addCSession(CounsellingSession cs){
        counsellingSessions.add(cs);
    }
}
