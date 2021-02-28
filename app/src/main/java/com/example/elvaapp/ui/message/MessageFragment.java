package com.example.elvaapp.ui.message;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.elvaapp.Login;
import com.example.elvaapp.R;
import com.example.elvaapp.UserInstance.LoginInstance;
import com.example.elvaapp.ui.home.HomeViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class MessageFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, container, false);
        return root;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout linearLayout = getActivity().findViewById(R.id.message);

        LocalDateTime startDateTime = LocalDateTime.of(2020, 12, 28, 12, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 2, 23, 4, 0);
        Random r = new Random();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (LocalDateTime currentDateTime = startDateTime; currentDateTime.isBefore(endDateTime); currentDateTime = currentDateTime.plusMinutes(r.nextInt(1440) + 1440 )) {
            String res = currentDateTime.format(sdf) + "  疑似摔倒！！ 请及时查看！！";
            TextView textView = new TextView(getActivity());
            textView.setTextSize(18);
            layoutParams.setMargins(0, 10, 10, 10);
            textView.setCompoundDrawablePadding(10);
            textView.setText(res);
            textView.setLayoutParams(layoutParams);
            linearLayout.addView(textView);
        }
    }
}