package com.app.core;

public class User {

    public String userId;
    public String userName;
    public Double userCredit;

    public User() {}

    public User(String _userId, String _userName, double _userCredit) {
        super();
        this.userId = _userId;
        this.userName = _userName;
        this.userCredit = _userCredit;
    }

    @Override
    public String toString() {
        return "userId[" + userId + "] userName[" + userName + "] with credit[" + userCredit + "]";
    }

}
