package ru.yandexmusiccasher;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void onUpdateButton(View v){
        checkForUpdates();
    }

    private void checkForUpdates(){
        final AppUpdating appUpdating = new AppUpdating(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int currentVersion = getResources().getInteger(R.integer.version);
                int updateVersion = appUpdating.getUpdateVersion();
                if(currentVersion<updateVersion){
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            appUpdating.update();
                            finish();
                        }
                    });
                } else {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, getString(R.string.no_updates), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

}
