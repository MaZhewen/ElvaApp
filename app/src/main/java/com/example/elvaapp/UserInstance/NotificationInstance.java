package com.example.elvaapp.UserInstance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.example.elvaapp.MainActivity;
import com.example.elvaapp.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class NotificationInstance extends BroadcastReceiver {
    private static NotificationInstance notificationInstance;
    private NotificationInstance(){}

    public static synchronized NotificationInstance getInstance()
    {
        if (notificationInstance == null)
        {
            notificationInstance = new NotificationInstance();
        }
        return notificationInstance;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        System.out.println("FUCK");
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context, "FFFF");
        builder.setSmallIcon(R.drawable.fade_red);
        builder.setContentTitle("紧急通知");
        builder.setContentText("用户疑似发生危险");
        builder.setTicker("用户疑似发生危险——TICK");
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.fade_red));
        builder.setSubText("附加消息");
        // NotificationChannel
        // builder.setDefaults(Notification.DEFAULT_ALL);//全部效果展示(震动，铃声，呼吸灯)
        //点击页面跳转
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(activity);
        //悬浮显示
        builder.setFullScreenIntent(activity,true);
        manager.notify(1,builder.build());
    }
}
