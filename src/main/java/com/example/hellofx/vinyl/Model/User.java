package com.example.hellofx.vinyl.Model;

public class User {
    private final String userID;
    private final String userName;

    public User(String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String toString() {
        return userName + " ID: " + userID;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User other = (User) o;
        return userID.equals(other.userID);
    }
}
