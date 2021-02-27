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
import com.example.elvaapp.R;
import com.example.elvaapp.UserInstance.LoginInstance;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button buttonLoginIn;

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
            getActivity().findViewById(R.id.button_user_bind).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.button_sensor_bind).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.button_paras_practice).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.button_model_practice).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.userhead).setVisibility(View.VISIBLE);
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