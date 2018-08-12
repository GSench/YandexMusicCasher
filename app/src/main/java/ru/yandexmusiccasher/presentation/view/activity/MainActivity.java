package ru.yandexmusiccasher.presentation.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import ru.yandexmusiccasher.R;

public class MainActivity extends ReceiveActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void startDownloadingPlaying() {
        System.out.println("Path appears to be: "+getSharedPreferences("preferences", MODE_PRIVATE).getString("music_dir", "null"));
    }

    public void onInfoButton(View v){
        new AlertDialog.Builder(this)
                .setTitle(R.string.guide)
                .setView(R.layout.guide_layout)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }
}
