package com.example.elvaapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SensorBind extends AppCompatActivity {

    private Integer sensorName = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.senser_bind);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 选择用户类型
        String searchingText = "正在搜索传感器.....";
        TextView textView = (TextView) findViewById(R.id.textView_select_senor_bind);
        textView.setText(searchingText);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Looper.prepare();
                String searchingText = "请选择传感器";
                TextView textView = (TextView) findViewById(R.id.textView_select_senor_bind);
                textView.setText(searchingText);
                Spinner spinner = (Spinner)findViewById(R.id.sensor_bind_spinner);
                String[] mItems = {"测试传感器1", "测试传感器2"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SensorBind.this, android.R.layout.simple_spinner_item, mItems);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
                // Looper.loop();

            }
        }, 3000);//3秒后执行Runnable中的run方法
    }

    // 样式选择监听器
    class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0)
                sensorName = 1;
            else if(position == 1)
                sensorName = 2;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void onReturnButtonClick(View v)
    {
        finish();
    }

    public void onBindButtonClick(View v)
    {
        Toast.makeText(getApplicationContext(),"绑定成功",Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(SensorBind.this, MainActivity.class);;
        //setResult(2, intent);
        finish();
    }
}