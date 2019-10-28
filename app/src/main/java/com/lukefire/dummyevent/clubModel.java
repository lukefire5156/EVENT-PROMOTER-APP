package com.lukefire.dummyevent;

public class clubModel {

    String name,event_url,EventUID;
    //constructer
    public clubModel(){}
    //getter and setter


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventUID() {
        return EventUID;
    }

    public void setEventUID(String EventUID) {
        this.EventUID = EventUID;
    }


    public String getEvent_url() {
        return event_url;
    }

    public void setEvent_url(String event_url) {
        this.event_url = event_url;
    }
}
