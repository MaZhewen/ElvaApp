package com.example.elvaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class UserBind extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_bind);
    }

    public void onReturnButtonClick(View v)
    {
        finish();
    }

    public void onBindButtonClick(View v)
    {
        EditText editBindUser = findViewById(R.id.editText_binduser);
        String username = editBindUser.getText().toString();
        if (username.length() == 0){
            Toast.makeText(getApplicationContext(),"请输入正确用户名！",Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(getApplicationContext(),"绑定成功",Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(UserBind.this, MainActivity.class);;
        //setResult(2, intent);
        finish();
    }
}