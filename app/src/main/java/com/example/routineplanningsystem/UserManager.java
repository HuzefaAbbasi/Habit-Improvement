package com.example.routineplanningsystem;

public class UserManager {
    private static UserManager instance;
    private String userName;
    // Add more user information fields as needed

    private UserManager() {
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
