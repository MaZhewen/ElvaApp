package com.example.elvaapp.UserInstance;

import android.content.Intent;

public class LoginInstance {
    private static LoginInstance loginInstance;

    public boolean hasLogin = false;
    private String username;
    private String password;
    public Integer userType;
    private LoginInstance(){}

    public static synchronized LoginInstance getInstance()
    {
        if (loginInstance == null)
        {
            loginInstance = new LoginInstance();
        }
        return loginInstance;
    }

    public void setUsernameAndPassword(String username, String password, Integer userType)
    {
        if( hasLogin == false)
        {
            this.username = username;
            this.password = password;
            this.userType = userType;
            hasLogin = true;
        }
    }
}
