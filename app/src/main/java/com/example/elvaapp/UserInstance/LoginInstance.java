package com.example.elvaapp.UserInstance;

public class LoginInstance {
    private static LoginInstance loginInstance;

    public boolean hasLogin = false;
    private String username;
    private String password;
    private LoginInstance(){}

    public static synchronized LoginInstance getInstance()
    {
        if (loginInstance == null)
        {
            loginInstance = new LoginInstance();
        }
        return loginInstance;
    }

    public void setUsernameAndPassword(String username, String password)
    {
        if( hasLogin == false)
        {
            this.username = username;
            this.password = password;
            hasLogin = true;
        }
    }
}
