package ru.yandexmusiccasher;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&!Tools.writeExternalPermGranted(this)){
            new AlertDialog.Builder(this)
                    .setMessage(R.string.get_permission_msg)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                        }
                    })
                    .setNeutralButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    checkAndRequestPermissions();
                }
            }
        }
    }

}
