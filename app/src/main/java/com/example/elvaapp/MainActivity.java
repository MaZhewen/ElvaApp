package com.example.elvaapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.elvaapp.Service.LongRunningService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.content.ContextCompat.getSystemService;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_message)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        intent = new Intent(this, LongRunningService.class);
        startService(intent);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                // notice();
                PopDangerDialog("用户疑似发生危险");
            }
        },2000);//延时1s执行
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        System.out.println(requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        List<Fragment> fragments = navHostFragment.getChildFragmentManager().getFragments();
        if (fragments.size() > 0)
        {
            for (Fragment mFragment : fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
//         BottomNavigationView navView = findViewById(R.id.nav_view);
//         navView.getMenu().add(0,3, 3, "消息");
//         MenuItem item = navView.getMenu().findItem(3);
//         item.setIcon(R.drawable.ic_search_black_24dp);
    }

    public void notice()
    {
        System.out.println("FUCK");
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("WTF","OLDMAN",NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(notificationChannel);
        Notification.Builder builder = new Notification.Builder(this, "WTF");
        builder.setSmallIcon(R.drawable.fade_red);
        builder.setContentTitle("紧急通知");
        builder.setContentText("用户疑似发生危险");
        builder.setTicker("用户疑似发生危险——TICK");
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.fade_red));
        builder.setSubText("附加消息");
        notificationChannel.enableVibration(true);
        notificationChannel.enableLights(true);
        /// notificationChannel.
       // builder.setDefaults(Notification.DEFAULT_ALL);//全部效果展示(震动，铃声，呼吸灯)
        //点击页面跳转
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(activity);
        //悬浮显示
        builder.setFullScreenIntent(activity,true);
        manager.notify(1,builder.build());
    }

    public void PopDangerDialog(String title)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("紧急通知");
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