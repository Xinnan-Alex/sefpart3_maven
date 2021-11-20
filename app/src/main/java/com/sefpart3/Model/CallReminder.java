package com.sefpart3.Model;

import java.util.*;

public class CallReminder {
    private Schedule schedule = new Schedule();
    private String link = "https://meet.google.com/new"; 

    public CallReminder(){}

    public Schedule getSchedule(){ return schedule; }
    public void setSchedule(String startTime, String endTime, String frequency){
        this.schedule = new Schedule(startTime, endTime, frequency);
    }

    public String getLink(){ return link; }
    public void setLink(String link){ this.link = link; }

    public String toString(){
        ArrayList<String> temp = new ArrayList<>();
        temp.add(schedule.getStartT());
        temp.add(schedule.getEndT());
        temp.add(schedule.getFreq());
        temp.add(link);

        return Database.makeString(temp);
    }
}
