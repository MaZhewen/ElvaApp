package com.example.elvaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elvaapp.UserInstance.LoginInstance;

public class RecogModelTrain extends AppCompatActivity {

    private Integer curr_index = 0;
    private String[] behaviors = {"站立", "行走", "坐", "躺", "弯腰", "上下楼梯"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognition_model_train);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        TextView behavior = findViewById(R.id.textView_activities);
        behavior.setText(behaviors[curr_index]);
    }

    public void onReturnButtonClick(View v)
    {
        finish();
    }

    public void onNextButtonClick(View v)
    {
        if (curr_index < behaviors.length - 1)
        {
            curr_index += 1;
            TextView behavior = findViewById(R.id.textView_activities);
            behavior.setText(behaviors[curr_index]);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"训练成功",Toast.LENGTH_LONG).show();
            finish();
        }
    }

}