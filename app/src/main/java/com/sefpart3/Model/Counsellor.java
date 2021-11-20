package com.sefpart3.Model;

import java.util.*;

public class Counsellor extends Account{
    private String name;
    private String phoneNo;
    private String DOB;
    private String licenseNo;
    private String description;

    public Counsellor(String email, String password, String name, String phoneNo, String licenseNo){
        super(email, password, "Counsellor");
        this.name = name;
        this.phoneNo = phoneNo;
        this.licenseNo = licenseNo;
    }

    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public String getPhoneNo(){ return phoneNo; }
    public void setPhoneNo(String phoneNo){ this.phoneNo = phoneNo; }

    public String getDOB(){ return DOB; }
    public void setDOB(String DOB){ this.DOB = DOB; }

    public String getLicenseNo(){ return licenseNo; }
    public void setLicenseNo(String licenseNo){ this.licenseNo = licenseNo; }

    public String getDesc(){ return description; }
    public void setDesc(String description){ this.description = description;}

    @Override
    public void write(boolean exist){ // used when create new object
        Database.writeData("Counsellor", toList());
    }

    @Override
    public List<String> toList(){
        return Arrays.asList(getEmail(), getPassword(), name, phoneNo, DOB, licenseNo, description);
    }
}
