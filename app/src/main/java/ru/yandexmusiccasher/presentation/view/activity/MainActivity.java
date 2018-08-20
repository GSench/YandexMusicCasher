package ru.yandexmusiccasher.presentation.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ru.yandexmusiccasher.R;
import ru.yandexmusiccasher.presentation.model.AMSOperations;
import ru.yandexmusiccasher.presentation.model.AMusicStorage;
import ru.yandexmusiccasher.presentation.presenter.HelloPresenter;
import ru.yandexmusiccasher.presentation.view.HelloView;

public class MainActivity extends AppCompatActivity implements HelloView {

    private final int PICK_PATH = 1;

    private HelloPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new HelloPresenter(new AMSOperations(this));
        presenter.setView(this);
        presenter.start();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == PICK_PATH)
            if (resultCode == RESULT_OK) {
                Uri treeUri = resultData.getData();
                getContentResolver().takePersistableUriPermission(treeUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                presenter.onPathReceived(new AMusicStorage(treeUri, this));
            } else presenter.onPathFailToReceive();
    }

    @Override
    public void pickPath() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.choose_path)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE), PICK_PATH);
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
