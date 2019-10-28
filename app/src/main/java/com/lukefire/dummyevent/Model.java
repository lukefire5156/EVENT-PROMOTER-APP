package com.lukefire.dummyevent;

public class Model {

    String name, Description,event_url, Logo_url,Registration_Link;
    //constructer
    public Model(){}
    //getter and setter


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//////////////
    public String getLogo_url() {
        return Logo_url;
    }

    public void setLogo_url(String Logo_url) {
        this.Logo_url = Logo_url;
    }
    //////
    public String getRegistration_Link() {
        return Registration_Link;
    }

    public void setRegistration_Link(String Registration_Link) {
        this.Logo_url = Registration_Link;
    }
//////////////
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEvent_url() {
        return event_url;
    }

    public void setEvent_url(String event_url) {
        this.event_url = event_url;
    }
}
