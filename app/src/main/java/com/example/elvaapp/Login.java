package com.example.elvaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.elvaapp.UserInstance.LoginInstance;

public class Login extends AppCompatActivity {

    private Integer userType = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 选择用户类型
        Spinner spinner = (Spinner)findViewById(R.id.user_type_spinner);
        String[] mItems = {"老年人", "监护人"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
    }

    // 样式选择监听器
    class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0)
                userType = 1;
            else if(position == 1)
                userType = 2;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
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
        LoginInstance.getInstance().setUsernameAndPassword(username, password, userType);
        Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Login.this, MainActivity.class);;
        setResult(2, intent);
        finish();
    }

}