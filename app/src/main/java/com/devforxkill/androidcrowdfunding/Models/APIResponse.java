package com.devforxkill.androidcrowdfunding.Models;

public class APIResponse {

    String status;
    String message;
    User data;

    /* ----- USER ----- */
    public User getUser() {
        return data;
    }

    public void setUser(User user) {
        this.data = user;
    }

    /* ----- STATUS ----- */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /* ----- MESSAGE ----- */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
