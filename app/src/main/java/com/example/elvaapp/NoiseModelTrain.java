package com.example.elvaapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elvaapp.UserInstance.LoginInstance;

public class NoiseModelTrain extends AppCompatActivity {

    private Integer userType = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noise_model_train);
    }

    public void onReturnButtonClick(View v)
    {
        finish();
    }

    public void onStartButtonClick(View v)
    {
        Toast.makeText(getApplicationContext(),"开始训练",Toast.LENGTH_LONG).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Looper.prepare();
                Toast.makeText(getApplicationContext(),"训练成功",Toast.LENGTH_LONG).show();
                finish();
                // Looper.loop();
            }
        }, 3000);//3秒后执行Runnable中的run方法
    }

    public void onEndButtonClick(View v)
    {
        Toast.makeText(getApplicationContext(),"结束训练",Toast.LENGTH_LONG).show();
        finish();
    }
}