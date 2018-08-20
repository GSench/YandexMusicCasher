package ru.yandexmusiccasher.presentation.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ru.yandexmusiccasher.R;
import ru.yandexmusiccasher.domain.interactor.DownloadCompleteInteractor;
import ru.yandexmusiccasher.presentation.model.AMSOperations;
import ru.yandexmusiccasher.presentation.model.AMusicStorage;
import ru.yandexmusiccasher.presentation.presenter.ReceivePresenter;
import ru.yandexmusiccasher.presentation.view.UrlReceivedView;
import ru.yandexmusiccasher.presentation.view.service.YandexDownloadService;

/**
 * Created by GSench on 02.06.2016.
 */
public class ReceiveActivity extends AppCompatActivity implements UrlReceivedView {

    private final int PICK_PATH = 1;

    private ReceivePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_activity);
        presenter = new ReceivePresenter(new AMSOperations(this));
        presenter.setView(this);
        presenter.start();
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

    @Override
    public void startDownloadingPlaying() {
        startService(new Intent(this, YandexDownloadService.class)
                .putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra(Intent.EXTRA_TEXT))
                .putExtra(YandexDownloadService.DOWNLOAD_PLAY_STRATEGY, DownloadCompleteInteractor.DOWNLOAD_PLAY));
        finish();
    }

    @Override
    public void startDownloading() {
        startService(new Intent(this, YandexDownloadService.class)
                .putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra(Intent.EXTRA_TEXT))
                .putExtra(YandexDownloadService.DOWNLOAD_PLAY_STRATEGY, DownloadCompleteInteractor.DOWNLOAD));
        finish();
    }

    @Override
    public void startPlaying() {
        startService(new Intent(this, YandexDownloadService.class)
                .putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra(Intent.EXTRA_TEXT))
                .putExtra(YandexDownloadService.DOWNLOAD_PLAY_STRATEGY, DownloadCompleteInteractor.PLAY));
        finish();
    }

    public void download(View view){
        presenter.download();
    }

    public void downloadPlay(View view){
        presenter.downloadPlay();
    }

    public void play(View view){
        presenter.play();
    }
}
