package com.sefpart3.Model;

import java.util.Arrays;
import java.util.List;

public class CounsellingSession {
    private Schedule schedule;
    private User user;
    private Boolean user_Joined = false;
    private Counsellor counsellor;
    private Boolean counsellor_Joined = false;
    private String date;
    private String reason;
    private String link = "https://meet.google.com/new";

    public CounsellingSession(User user, Counsellor counsellor, String date, String reason, String time, String link){
        this.user = user;
        this.counsellor = counsellor;
        this.date = date;
        this.reason = reason;
        setSchedule(time);
        this.link = link;
    }

    public Schedule getSchedule(){ return schedule; }
    public void setSchedule(String time){ this.schedule = new Schedule(time); }

    public User getUser(){ return user; }

    public Counsellor getCounsellor(){ return counsellor; }

    public String getDate(){ return date; }

    public String getReason(){ return reason; }

    public String getLink(){ return link; }

    public Boolean getJoined(String role){
        if(role.equals("User"))
            return user_Joined;
        else
            return counsellor_Joined;
    }

    public void setJoined(String role, boolean status){
        if(role.equals("User"))
            user_Joined = status;
        else
            counsellor_Joined = status;
    }

    public void write(){
        Database.writeData("Counselling Session", toList());
    }

    public List<String> toList(){
        return Arrays.asList(user.getEmail(), counsellor.getEmail(), date, reason, schedule.getTime() , 
                             link, Boolean.toString(user_Joined), Boolean.toString(counsellor_Joined));
    }
}
