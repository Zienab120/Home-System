package com.example.pervasive_projcect;



public class Event {
    String ID;

    String activityName;
    String event;
    String timeStamp;

    public Event(String ID, String activityName, String event, String timeStamp) {
        this.ID = ID;
        this.activityName = activityName;
        this.event = event;
        this.timeStamp = timeStamp;
    }
    public Event()
    {

    }
    public Event(Event e)
    {
        this.ID=e.ID;
        this.activityName = e.activityName;
        this.event = e.event;
        this.timeStamp = e.timeStamp;
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
