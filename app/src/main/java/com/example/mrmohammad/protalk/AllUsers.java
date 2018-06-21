package com.example.mrmohammad.protalk;

public class AllUsers {

    public String user_name;
    public String user_status;
    public String img;

    public AllUsers(){

    }


    public AllUsers(String user_name, String user_status, String img) {
        this.user_name = user_name;
        this.user_status = user_status;
        this.img = img;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
