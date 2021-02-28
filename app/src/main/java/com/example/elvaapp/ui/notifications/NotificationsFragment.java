package com.example.elvaapp.ui.notifications;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.elvaapp.R;
import com.example.elvaapp.ui.dashboard.DashboardFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private Handler tickHandler;
    private Timer timer;
    private TimerTask timerTask;
    private static final int UPDATE_LINE_CHART = 0;
    private Random random = new Random();
    private int x = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        LineChart mLineChar = (LineChart)getActivity().findViewById(R.id.mLineChart);
        //设置手势滑动事件
        // mLineChar.setOnChartGestureListener(getActivity());
        //设置数值选择监听
        // mLineChar.setOnChartValueSelectedListener(this);
        //后台绘制
        mLineChar.setDrawGridBackground(false);
        //设置描述文本
        mLineChar.getDescription().setEnabled(false);
        //设置支持触控手势
        mLineChar.setTouchEnabled(true);
        //设置缩放
        mLineChar.setDragEnabled(true);
        //设置推动
        mLineChar.setScaleEnabled(true);
        //如果禁用,扩展可以在x轴和y轴分别完成
        mLineChar.setPinchZoom(true);

        // DrawLineChart();
        UpdateTimer();

        // 选择要显示的传感器数据
        Spinner spinner = (Spinner)getActivity().findViewById(R.id.sensor_spinner);
        String[] mItems = {"传感器1", "传感器2", "传感器3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new NotificationsFragment.SpinnerOnItemSelectedListener());
        // spinner.setVisibility(View.INVISIBLE);
    }

    // 样式选择监听器
    class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            LineChart mLineChart = (LineChart)getActivity().findViewById(R.id.mLineChart);
            if (mLineChart.getData() != null)
            {
                mLineChart.getData().clearValues();
                mLineChart.clear();
                InitLineChart();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        timerTask.cancel();
    }


    private void UpdateTimer()
    {
        tickHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what == UPDATE_LINE_CHART)
                    UpdateSensorData();
            }
        };
        timerTask = new TimerTask() {
            @Override
            public void run() {
                tickHandler.sendEmptyMessage(UPDATE_LINE_CHART);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
    }

    private void UpdateSensorData()
    {
        float half_range = 5f;
        ArrayList<Float> sensorDatas = new ArrayList<>();
        for (int i = 0; i < 9; ++i)
            sensorDatas.add(random.nextFloat() * half_range * 2 - half_range);
        UpdateSensorDataText(sensorDatas);
        AddToLineChart(sensorDatas);
    }
    private void UpdateSensorDataText(ArrayList<Float> sensorDatas)
    {
        Integer[] textViewIds = {R.id.textView_sensor_1_x, R.id.textView_sensor_1_y, R.id.textView_sensor_1_z,
                                R.id.textView_sensor_2_x, R.id.textView_sensor_2_y, R.id.textView_sensor_2_z,
                                R.id.textView_sensor_3_x, R.id.textView_sensor_3_y, R.id.textView_sensor_3_z};
        float sumVal = 0;
        for (int i = 0; i < textViewIds.length; ++i)
        {
            TextView textView = getActivity().findViewById(textViewIds[i]);
            if (textView != null)
            {
                textView.setText(Float.toString(sensorDatas.get(i)));
                sumVal += sensorDatas.get(i);
            }
        }
        TextView result = getActivity().findViewById(R.id.textView_current_behavior_result);
        String[] behaviors = {"站立", "行走", "坐", "躺", "弯腰", "上下楼梯"};
        Integer index = new Random().nextInt(6);
        result.setText(behaviors[index]);
    }

    private void UpdateLineChart()
    {
        LineChart mLineChart = (LineChart)getActivity().findViewById(R.id.mLineChart);
        LineDataSet lineDataSet;
        if (mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0)
        {
            lineDataSet = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            lineDataSet.addEntry(new Entry(x++, random.nextInt(100)));
            lineDataSet.notifyDataSetChanged();
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
            if(x > 10)
            {
                mLineChart.setVisibleXRangeMaximum(10);
                mLineChart.moveViewToX(x - 10);
            }
            else
                mLineChart.fitScreen();
        }
        else
        {
            DrawLineChart();
            x = 0;
        }
    }

    private void AddToLineChart(ArrayList<Float> sensorDatas)
    {
        Spinner spinner = (Spinner)getActivity().findViewById(R.id.sensor_spinner);
        ArrayList<Entry> values = new ArrayList<Entry>();
        int start_index = 0;
        if(spinner.getSelectedItemPosition() == 1)
            start_index = 3;
        else if(spinner.getSelectedItemPosition() == 2)
            start_index = 6;
        for(int i = 0; i < start_index + 3; ++i)
            values.add(new Entry(x, sensorDatas.get(i)));
        x++;
        LineChart mLineChart = (LineChart)getActivity().findViewById(R.id.mLineChart);
        if (mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0)
        {
            for(int i = 0; i < mLineChart.getData().getDataSetCount(); ++i)
            {
                LineDataSet lineDataSet= (LineDataSet) mLineChart.getData().getDataSetByIndex(i);
                lineDataSet.addEntry(values.get(i));
                lineDataSet.notifyDataSetChanged();
            }
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
            if(x > 10)
            {
                mLineChart.setVisibleXRangeMaximum(10);
                mLineChart.moveViewToX(x - 10);
            }
            else
                mLineChart.fitScreen();
        }
        else
            InitLineChart();
    }

    private void InitLineChart()
    {
        LineChart mLineChart = (LineChart)getActivity().findViewById(R.id.mLineChart);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        int[] colors = {Color.RED, Color.GREEN, Color.BLUE};
        String[] names = {"X轴加速度", "Y轴加速度", "Z轴加速度"};
        for (int i = 0; i < 3; ++i)
        {
            LineDataSet lineDataSet = new LineDataSet(new ArrayList<Entry>(), names[i]);
            lineDataSet.setDrawIcons(false);
            lineDataSet.enableDashedLine(10f, 5f, 0f);

            // 颜色
            lineDataSet.setColor(colors[i]);
            lineDataSet.setCircleColor(colors[i]);
            // customize legend entry
            lineDataSet.setFormLineWidth(1f);
            lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            lineDataSet.setFormSize(15.f);

            // 文本大小
            lineDataSet.setValueTextSize(9f);

            // draw selection line as dashed
            lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            // lineDataSet.setDrawFilled(true);
            lineDataSet.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet iLineDataSet, LineDataProvider lineDataProvider) {
                    return mLineChart.getAxisLeft().getAxisMinimum();
                }
            });
            dataSets.add(lineDataSet);
        }

        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        //默认动画
        mLineChart.animateX(2500);
        //刷新
        mLineChart.invalidate();
        // 得到这个文字
        Legend l = mLineChart.getLegend();
        // 修改文字 ...
        l.setForm(Legend.LegendForm.LINE);
        x = 0;
    }

    private void DrawLineChart()
    {
        ArrayList<Entry> values = new ArrayList<Entry>();
        values.add(new Entry(0, 0));

        LineChart mLineChart = (LineChart)getActivity().findViewById(R.id.mLineChart);
//        for (int i = 0; i < count; i++) {
//
//            float val = (float) (Math.random() * range) - 30;
//            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.star)));
//        }
        LineDataSet lineDataSet;
        if (mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0)
        {
            lineDataSet = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            lineDataSet.notifyDataSetChanged();
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        }
        else
        {
            lineDataSet = new LineDataSet(values, "DATASET");
            lineDataSet.setDrawIcons(false);
            lineDataSet.enableDashedLine(105, 5f, 0f);
            // 颜色
            lineDataSet.setColor(Color.BLACK);
            lineDataSet.setCircleColor(Color.BLACK);
            // customize legend entry
            lineDataSet.setFormLineWidth(1f);
            lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            lineDataSet.setFormSize(15.f);

            // 文本大小
            lineDataSet.setValueTextSize(9f);

            // draw selection line as dashed
            lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet iLineDataSet, LineDataProvider lineDataProvider) {
                    return mLineChart.getAxisLeft().getAxisMinimum();
                }
            });
        }

        // set color of filled area
        if (Utils.getSDKInt() >= 18)
        {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
            lineDataSet.setFillDrawable(drawable);
        }
        else
            lineDataSet.setFillColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        //默认动画
        mLineChart.animateX(2500);
        //刷新
        mLineChart.invalidate();
        // 得到这个文字
        Legend l = mLineChart.getLegend();
        // 修改文字 ...
        l.setForm(Legend.LegendForm.LINE);
    }

    // private void
}