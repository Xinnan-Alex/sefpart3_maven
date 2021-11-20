package com.sefpart3.Model;

import java.util.List;

import io.opencensus.stats.View.Name;

public abstract class Account {
    private String email;
    private String password;
    private String role;

    public Account(String email, String password, String role){
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }

    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }

    public String getRole(){ return role; }

    public abstract void write(boolean exist);
    public abstract List<String> toList(); // for converting object into List<String> for storing into database


}
