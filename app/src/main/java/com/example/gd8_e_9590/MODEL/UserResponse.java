package com.example.gd8_e_9590.MODEL;

import com.example.gd8_e_9590.MODEL.UserDAO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    @SerializedName("data")
    @Expose
    private List<UserDAO> users = null;

    @SerializedName("message")
    @Expose
    private String message;

    public List<UserDAO> getUsers() {
        return users;
    }

    public String getMessage() {
        return message;
    }

    public void setUsers(List<UserDAO> users) {
        this.users = users;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
