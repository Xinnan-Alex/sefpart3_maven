package com.sefpart3.Model;

import java.util.*;

public class Message {
    private Schedule schedule = new Schedule();
    private String[] messages = {"I am fine", "I am not fine", "I am not sure"};

    public Message(){} // used when a new user is created
    
    public void addMessage(String message, int index){
        messages[index] = message;
    }

    public String getMessages(int index){
        return messages[index];
    }

    public Schedule getSchedule(){
        return schedule;
    }

    public void setSchedule(String startTime, String endTime, String frequency){
        schedule = new Schedule(startTime, endTime, frequency);
    }

    public String toString(){
        ArrayList<String> temp = new ArrayList<>();
        temp.add(schedule.getStartT());
        temp.add(schedule.getEndT());
        temp.add(schedule.getFreq());
        
        for(int i = 0; i < 3; i++) temp.add(messages[i]);

        return Database.makeString(temp);
    }

}
