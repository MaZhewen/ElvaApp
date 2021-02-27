package com.example.elvaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elvaapp.UserInstance.LoginInstance;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onReturnButtonClick(View v)
    {
        finish();
    }

    public void onLoginButtonClick(View v)
    {
        EditText editUsername = findViewById(R.id.editText_username);
        EditText editPassword = findViewById(R.id.editText_password);
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        LoginInstance.getInstance().setUsernameAndPassword(username, password);
        Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Login.this, MainActivity.class);;
        setResult(2, intent);
        finish();
    }

}