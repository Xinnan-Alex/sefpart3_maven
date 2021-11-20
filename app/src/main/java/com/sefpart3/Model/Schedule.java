package com.sefpart3.Model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Schedule{
    ArrayList<String> time = new ArrayList<>();
    String startTime = "00:00";
    String endTime = "00:00";
    String frequency = "1";

    public Schedule(){
        loop(startTime, endTime, Integer.parseInt(frequency));
    }

    public Schedule(String time){ // for Counselling Session
        this.time.add(time);
    }

    public Schedule(String startTime, String endTime, String frequency){ // for Message/Call Reminder
        this.startTime = startTime;
        this.endTime = endTime;
        this.frequency = frequency;
        loop(startTime, endTime, Integer.parseInt(frequency));
    }

    public String getStartT(){ return startTime; }
    public String getEndT(){ return endTime; }
    public String getFreq(){ return frequency; }
    public String getTime(){ return time.get(0); } // for counselling session

    public boolean tick(){
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("HH:mm");
        
        String currentTime = LocalTime.now().format(dft);
        int currentSecond = LocalTime.now().getSecond();
        for(int i = 0; i < time.size(); i++){
            if(time.get(i).equals(currentTime) && (currentSecond < 5)){
                System.out.println("it's now");
                return true;
            }
        }
        return false;
    }

    public void loop(String startTime, String endTime, int frequency){ // to generate list of time 
        int start = totalMinutes(startTime); // 12:30 -> 750
        int end = totalMinutes(endTime); // 18:20 -> 1100
        if(start > end) // if 23:00 - 02:00, it will be converted to 23:00 - 26:00(24+2)
            end += 1440;
        int numOfAlarms = (end - start) / (frequency * 60); // (1100 - 750) / (every 2 hours * 60)
                                                           // 350 / 120 = 2, so between 12:30 to 18:20, 
                                                           // there will only be 2 alerts, which is 14:30 and 16:30

        for(int i = 0; i < numOfAlarms + 2; i++){ // including start and end
            if(i == numOfAlarms + 1){ // final loop
                time.add(endTime);
                break;
            }
            int currentHour = Integer.parseInt(startTime.substring(0,2)) + (frequency * i); // 12 + (2 * i)
            if(currentHour >= 24)
                currentHour -= 24;

            int currentMinute = Integer.parseInt(startTime.substring(3,5));
            String currentTime = convert(currentHour) + ":" + convert(currentMinute);

            if(!(currentTime.equals(endTime)))
                time.add(currentTime);
        }
    }

    private int totalMinutes(String time){
        // 19:23
        int hours = Integer.parseInt(time.substring(0, 2)); // 19
        int minutes = Integer.parseInt(time.substring(3, 5)); // 23
        int total = hours * 60 + minutes; // 19 * 60 + 23

        return total;
    }

    private String convert(int time){ // convert 2:0 to 02:00
        String result = Integer.toString(time);
        if(time < 10)
            result = "0" + result;

        return result;
    }
}