package com.sefpart3.Model;

import java.util.*;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;

public class Database{

  public static void writeData(String tableName, List<String> Entry){
    String fileName = tableName + ".csv";
    String filePath = "app/src/main/java/com/sefpart3/Database/"+fileName;
    try{
        FileWriter pw = new FileWriter(filePath,true);
        System.out.println("Writing " + fileName);
        for(int i = 0; i < Entry.size(); i++){
              pw.append(Entry.get(i));
              if(i == Entry.size() - 1){
                pw.append("\n");
              }
              else{
                pw.append(",");
              }
        }
        pw.flush();
        pw.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
    
  }

  public static void writeAllData(String tableName, List<List<String>> All){
    String fileName = tableName + ".csv";
    String filePath = "app/src/main/java/com/sefpart3/Database/"+fileName;
    try{
        FileWriter pw = new FileWriter(filePath);
        System.out.println("Writing " + fileName);
        for(int i = 0; i < All.size(); i++){
            for(int j = 0; j < All.get(i).size(); j++){
                pw.append(All.get(i).get(j));
                if(j == All.get(i).size() - 1) pw.append("\n");
                else pw.append(",");
            }
        }
        pw.flush();
        pw.close();
    }
    catch(Exception e){
        e.printStackTrace();
    }
    
  }

  public static List<String> parseArray(String list){
    list = list.replace("[", "");
    list = list.replace("]", "");
    String[] data = list.split("~");
    List<String> temp = Arrays.asList(data);
    return temp;
  }

  public static String makeString(List<String> data){
    String out = "[";
    for(int i = 0; i < data.size(); i++){
      out += data.get(i);
      if(i != (data.size() - 1)){
        out += "~";
      }
    }
    out += "]";
    return out;
  }

  public static List<List<String>> readData(String tableName){
    String fileName = tableName + ".csv";
    String filePath = "app/src/main/java/com/sefpart3/Database/"+fileName;
    List<List<String>> table = new ArrayList<List<String>>();
    try{
        String row = null;
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            List<String> temp = Arrays.asList(data);
            table.add(temp);
        }
        csvReader.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return table;
  }
}
