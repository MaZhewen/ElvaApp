package com.example.elvaapp.ui.dashboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.example.elvaapp.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private LocalDate dateSelect = null;
    private LocalTime startTime = null;
    private LocalTime endTime = null;

    private Button buttonSelectDate = null;
    private Button buttonSelectStartTime = null;
    private Button buttonSelectEndTime = null;
    private Button buttonConfirm = null;
    private String[] behaviors = {"站立", "行走", "坐", "躺", "弯腰", "上下楼梯"};
    private String[] healthEvaluate = {"健康", "亚健康", "非健康" };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        if (buttonSelectDate != null)
            return;
        buttonSelectDate = (Button)getActivity().findViewById(R.id.button_date_select);
        buttonSelectStartTime = (Button)getActivity().findViewById(R.id.button_starttime_select);
        buttonSelectEndTime = (Button)getActivity().findViewById(R.id.button_endtime_select);
        buttonConfirm = (Button)getActivity().findViewById(R.id.button_confirm);

        buttonSelectDate.setOnClickListener(new ButtonOnCickListener());
        buttonSelectStartTime.setOnClickListener(new ButtonOnCickListener());
        buttonSelectEndTime.setOnClickListener(new ButtonOnCickListener());
        buttonConfirm.setOnClickListener(new ButtonOnCickListener());

        // 选择显示样式
        Spinner spinner = (Spinner)getActivity().findViewById(R.id.show_spinner);
        String[] mItems = {"饼图", "数据表"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
        spinner.setVisibility(View.INVISIBLE);

        getActivity().findViewById(R.id.text_health_evaluate).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.text_health_evaluate_result).setVisibility(View.INVISIBLE);
    }

    // 按钮点击监听器
    class ButtonOnCickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            Calendar calendar=Calendar.getInstance();
            if (view.getId() == R.id.button_date_select)
            {
                // 弹窗
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String data = String.format("%d-%d-%d", year, month+1, dayOfMonth);
                                TextView editText = getActivity().findViewById(R.id.textView_date_data);
                                editText.setText(data);
                                dateSelect = LocalDate.of(year, month+1, dayOfMonth);

                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
            else if (view.getId() == R.id.button_starttime_select || view.getId() == R.id.button_endtime_select)
            {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker timePickerView, int hourOfDay, int minute) {
                                String date = String.format("%d:%d", hourOfDay, minute);
                                int editTextId = -1;
                                if (view.getId() == R.id.button_starttime_select)
                                {
                                    editTextId = R.id.textView_starttime_data;
                                    startTime = LocalTime.of(hourOfDay, minute);
                                }
                                else
                                {
                                    editTextId = R.id.textView_endtime_data;
                                    endTime = LocalTime.of(hourOfDay, minute);
                                }
                                TextView editText = getActivity().findViewById(editTextId);
                                editText.setText(date);
                            }
                        },0, 0, true);
                timePickerDialog.show();
            }
            else if (view.getId() == R.id.button_confirm)
            {
                OnConfirmButtonClick(view);
            }
        }

        public void OnConfirmButtonClick(View view)
        {
            if (dateSelect == null || startTime == null || endTime == null)
            {
                PopErrorDialog("请选择日期和时间");
                return;
            }
            else if (startTime.compareTo(endTime) >= 0)
            {
                PopErrorDialog("请输入正确时间");
                return;
            }
            Spinner spinner = getActivity().findViewById(R.id.show_spinner);
            spinner.setVisibility(View.VISIBLE);
            spinner.setSelection(0);

            getActivity().findViewById(R.id.text_health_evaluate).setVisibility(View.VISIBLE);
            TextView ret = getActivity().findViewById(R.id.text_health_evaluate_result);
            ret.setVisibility(View.VISIBLE);
            int index = new Random().nextInt(2);
            ret.setText(healthEvaluate[index]);
        }

        public void PopErrorDialog(String title)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("错误");
            builder.setMessage(title);
            builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.show();
        }
    }

    // 样式选择监听器
    class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0)
                InitPieChart();
            else if(position == 1)
                InitTableChart();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private boolean check_can_shown_chart()
    {
        if (dateSelect == null || startTime == null || endTime == null)
            return false;
        else if (startTime.compareTo(endTime) >= 0)
            return false;
        else
            return true;
    }

    public void InitPieChart()
    {
        if (!check_can_shown_chart())
            return;
        ArrayList<String> names = new ArrayList<String>();
        for(int i = 0; i < behaviors.length; ++i)
        {
            names.add(behaviors[i]);
        }
        ArrayList<Double> values = new ArrayList<Double>(Arrays.asList(16.3, 18.5, 18.3, 40.7, 1.2, 5.0));
        ArrayList<Integer> colors = new ArrayList<Integer>(Arrays.asList(Color.BLUE, Color.GREEN, Color.MAGENTA, Color.RED, Color.CYAN, Color.YELLOW));

        CategorySeries dataSet = buildCategoryDataset("行为统计", names, values);
        DefaultRenderer renderer = buildCategoryRenderer(colors);

        LinearLayout linearLayout = (LinearLayout)getActivity().findViewById(R.id.linear_layout);
        if (linearLayout != null)
        {
            GraphicalView graphicalView = ChartFactory.getPieChartView(getContext(), dataSet, renderer);
            graphicalView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.removeAllViews();
            linearLayout.addView(graphicalView);
        }
    }

    private CategorySeries buildCategoryDataset(String title, ArrayList<String> names, ArrayList<Double> values)
    {
        if (names.size() != values.size())
            return null;
        CategorySeries series = new CategorySeries(title);
        double sumVal = 0;
        for(int i = 0; i < values.size(); ++i)
            sumVal += values.get(i);
        for(int i = 0; i < names.size(); ++i)
            series.add(names.get(i) + values.get(i), values.get(i) / sumVal);
        return series;
    }

    private DefaultRenderer buildCategoryRenderer(ArrayList<Integer> colors)
    {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLegendTextSize(35);//设置左下角标注文字的大小
        renderer.setLabelsTextSize(25);//饼图上标记文字的字体大小
        renderer.setLabelsColor(Color.BLACK);//饼图上标记文字的颜色
        renderer.setPanEnabled(false);
        //renderer.setDisplayValues(true);//显示数据

        for(int color : colors)
        {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            //设置百分比
            //r.setChartValuesFormat(NumberFormat.getPercentInstance());
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    private void InitTableChart()
    {
        if(!check_can_shown_chart())
            return;
        final Column<String> times = new Column<>("时间", "time");
        final Column<String> activities = new Column<>("行为", "activity");
        final Column<Boolean> dangers = new Column<>("危险", "danger");

        @SmartTable(name="行为信息列表")
        class UserInfo{
            @SmartColumn(id=1, name="时间")
            private  String time;
            @SmartColumn(id=2, name="行为")
            private  String activity;
            @SmartColumn(id=3, name="危险")
            private  Boolean danger;
            UserInfo(String time, String activity, Boolean danger){
                this.time = time;
                this.activity = activity;
                this.danger = danger;
            }
        }

        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.of(dateSelect, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(dateSelect, endTime);
        Random r = new Random();

        for(LocalDateTime currentDateTime = startDateTime; currentDateTime.isBefore(endDateTime) || currentDateTime.isEqual(endDateTime); currentDateTime= currentDateTime.plusMinutes(30)){
            UserInfo userInfo = null;
            Integer index = r.nextInt(6);
            if(index == 3)
                index -= 1;
            userInfo = new UserInfo(df.format(currentDateTime), behaviors[index], false);
            userInfos.add(userInfo);

        }
        TableData tableData = new TableData<>("测试",userInfos,times,activities,dangers);
        // com.bin.david.form.core.SmartTable smartTable = (com.bin.david.form.core.SmartTable<UserInfo>)getActivity().findViewById(R.id.table);

        LinearLayout linearLayout = (LinearLayout)getActivity().findViewById(R.id.linear_layout);
        if(linearLayout != null)
        {
            com.bin.david.form.core.SmartTable<UserInfo> smartTable = new com.bin.david.form.core.SmartTable<UserInfo>(getContext());
            smartTable.setTableData(tableData);
            smartTable.getConfig().setShowXSequence(false);
            smartTable.getConfig().setShowYSequence(false);
            ScrollView scrollView = new ScrollView(getContext());
            scrollView.addView(smartTable);
            // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            // scrollView.setLayoutParams(lp);
            linearLayout.removeAllViews();
            linearLayout.addView(scrollView);
        }
    }
}