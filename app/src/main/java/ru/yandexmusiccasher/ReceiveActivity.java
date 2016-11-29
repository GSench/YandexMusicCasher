package ru.yandexmusiccasher;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by GSench on 02.06.2016.
 */
public class ReceiveActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!checkAndRequestPermissions()) startCashing();
    }

    private boolean checkAndRequestPermissions(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&!Tools.writeExternalPermGranted(this)){
            new AlertDialog.Builder(this)
                    .setMessage(R.string.get_permission_msg)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            ActivityCompat.requestPermissions(ReceiveActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
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
        return Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&!Tools.writeExternalPermGranted(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCashing();
                } else {
                    Toast.makeText(ReceiveActivity.this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    checkAndRequestPermissions();
                }
            }
        }
    }

    private void startCashing(){
        startService(new Intent(this, YandexDownloadService.class).putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra(Intent.EXTRA_TEXT)));
        finish();
    }
}
