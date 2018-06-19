package com.example.mrmohammad.protalk;
public class Message {
    private String  message;
    private boolean msgme;



    int hour;
    int date;

    public Message(String message,boolean msgme, int hour, int date) {

        this.message = message;

        this.msgme = msgme;


        this.hour = hour;
        this.date=date;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getMsgMe(){
        return msgme;
    }

    public void setMsgme(boolean msgme){
        this.msgme = msgme;
    }




    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getHour() {
        return hour ;
    }
    public int getDate() {
        return date ;
    }

    public void setDate(int date) {
        this.date = date;
    }

}