package com.example.elvaapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.elvaapp.Login;
import com.example.elvaapp.NoiseModelTrain;
import com.example.elvaapp.R;
import com.example.elvaapp.RecogModelTrain;
import com.example.elvaapp.SensorBind;
import com.example.elvaapp.UserBind;
import com.example.elvaapp.UserInstance.LoginInstance;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button buttonLoginIn;
    private Button buttonUserBind;
    private Button buttonSensorBind;
    private Button buttonRecogModelTrain;
    private Button buttonNoiseModelTrain;
    private String[] buttonsOlder = {"传感器绑定", "识别模型训练", "模型参数训练"};
    private String[] buttonsGuarder = {"老年人绑定"};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        buttonLoginIn = (Button) getActivity().findViewById(R.id.button_login);
        buttonUserBind = (Button) getActivity().findViewById(R.id.button_user_bind);
        buttonSensorBind = (Button) getActivity().findViewById(R.id.button_sensor_bind);
        buttonRecogModelTrain = (Button) getActivity().findViewById(R.id.button_model_practice);
        buttonNoiseModelTrain = (Button) getActivity().findViewById(R.id.button_paras_practice);

        // 登录
        buttonLoginIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Login.class);
                getActivity().startActivityForResult(intent, 2);
            }
        });

        // 用户绑定
        buttonUserBind.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginInstance loginInstance = LoginInstance.getInstance();
                boolean hasLogin = loginInstance.hasLogin;
                if(hasLogin == true)
                {
                    if(loginInstance.userType == 1) // 老年人
                    {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), SensorBind.class);
                        getActivity().startActivityForResult(intent, 2);
                    }
                    else if(loginInstance.userType == 2) // 监护者
                    {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), UserBind.class);
                        getActivity().startActivityForResult(intent, 2);
                    }
                }
            }
        });

//        // 传感器绑定
//        buttonSensorBind.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), SensorBind.class);
//                getActivity().startActivityForResult(intent, 2);
//            }
//        });

        // 识别模型训练
        buttonSensorBind.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RecogModelTrain.class);
                getActivity().startActivityForResult(intent, 2);
            }
        });

        // 噪声模型训练
        buttonRecogModelTrain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(getActivity(), NoiseModelTrain.class);
                getActivity().startActivityForResult(intent, 2);
            }
        });

        ImageView imageView = getActivity().findViewById(R.id.userhead);
        // imageView.
        updateLoginState();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2)
        {
            updateLoginState();
        }
    }

    public void updateLoginState()
    {
        LoginInstance loginInstance = LoginInstance.getInstance();
        boolean hasLogin = loginInstance.hasLogin;
        if(hasLogin == true)
        {
            buttonLoginIn.setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.userhead).setVisibility(View.VISIBLE);
            if(loginInstance.userType == 1) // 老年人
            {
                Button btn1 = getActivity().findViewById(R.id.button_user_bind);
                btn1.setVisibility(View.VISIBLE);
                btn1.setText("传感器绑定");
                Button btn2 = getActivity().findViewById(R.id.button_sensor_bind);
                btn2.setVisibility(View.VISIBLE);
                btn2.setText("识别模型训练");
                Button btn3 = getActivity().findViewById(R.id.button_model_practice);
                btn3.setVisibility(View.VISIBLE);
                btn3.setText("模型参数训练");
            }
            else if(loginInstance.userType == 2) // 监护者
            {
                Button btn1 = getActivity().findViewById(R.id.button_user_bind);
                btn1.setVisibility(View.VISIBLE);
                btn1.setText("老年人绑定");
            }
        }
        else
        {
            getActivity().findViewById(R.id.button_user_bind).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.button_sensor_bind).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.button_paras_practice).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.button_model_practice).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.userhead).setVisibility(View.INVISIBLE);
            buttonLoginIn.setVisibility(View.VISIBLE);
        }
    }
}