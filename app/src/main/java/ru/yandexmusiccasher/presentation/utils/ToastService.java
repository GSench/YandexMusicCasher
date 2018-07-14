package ru.yandexmusiccasher.presentation.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by GSench Сенченок on 29.12.2015.
 */
public class ToastService extends Service {

    public static final String MESSAGE = "message";
    public static final String DURATION = "duration";

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, intent.getStringExtra(MESSAGE), intent.getIntExtra(DURATION, Toast.LENGTH_LONG)==Toast.LENGTH_LONG ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        stopSelf();
        return START_REDELIVER_INTENT;
    }

    public static void show(String text, int duration, Context context){
        Intent toast = new Intent(context, ToastService.class);
        toast.putExtra(ToastService.MESSAGE, text);
        toast.putExtra(ToastService.DURATION, duration);
        context.startService(toast);
    }

    public static void show(String text, Context context){
        Intent toast = new Intent(context, ToastService.class);
        toast.putExtra(ToastService.MESSAGE, text);
        context.startService(toast);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
