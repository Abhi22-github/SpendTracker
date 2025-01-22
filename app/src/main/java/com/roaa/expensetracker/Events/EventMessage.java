package com.roaa.expensetracker.Events;

public class EventMessage {

    public int eventCode;
    public String message;

    public EventMessage(int eventCode,String message){
        this.eventCode = eventCode;
        this.message = message;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
