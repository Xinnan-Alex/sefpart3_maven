package com.sefpart3.Model;

public class Google {
    private String email = "";
    private String id = "";
    private String name = "";
    private String pfpUrl = "";

    public Google() {
    }

    public Google(String email, String id, String name, String pfpUrl) {
        this.email = email;
        this.id = id;
        this.name = name;
        this.pfpUrl = pfpUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPfpUrl() {
        return pfpUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPfpUrl(String pfpUrl) {
        this.pfpUrl = pfpUrl;
    }
}