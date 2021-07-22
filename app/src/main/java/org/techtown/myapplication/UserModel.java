package org.techtown.myapplication;

public class UserModel {
    public String userName;
    public String uid;
    public String pushToken;
    public String email;

    public String getUserName() {
        return userName;
    }

    public String getUid() {
        return uid;
    }

    public String getPushToken() {
        return pushToken;
    }

    public String getEmail() {
        return email;
    }

    public UserModel(String userName, String pushToken, String email) {
        this.userName = userName;
        this.pushToken = pushToken;
        this.email = email;
    }
}
