package ru.yandexmusiccasher.presentation.view.service;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import ru.yandexmusiccasher.R;
import ru.yandexmusiccasher.presentation.AndroidInterface;
import ru.yandexmusiccasher.presentation.presenter.DownloadCompletePresenter;
import ru.yandexmusiccasher.presentation.utils.ToastService;
import ru.yandexmusiccasher.presentation.view.DownloadCompleteView;

/**
 * Created by GSench on 02.06.2016.
 */
public class DownloadCompleteReceiver extends BroadcastReceiver implements DownloadCompleteView {

    private Context context;
    private Intent intent;
    private DownloadCompletePresenter presenter;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) return;

        this.context=context;
        this.intent=intent;

        presenter = new DownloadCompletePresenter(new AndroidInterface(context));
        presenter.setView(this);
        presenter.start();
    }

    @Override
    public String getDownloadedFileUri(){
        String path = null;
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL != c.getInt(columnIndex)) return null;
            else {
                int uriIndex = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                path = c.getString(uriIndex);
            }
        }
        return path;
    }

    @Override
    public void downloadError() {
        ToastService.show(context.getString(R.string.download_error), context);
    }

    @Override
    public void copyingError() {
        ToastService.show(context.getString(R.string.copying_error), context);
    }

    @Override
    public void unableToDeleteCashFile() {
        ToastService.show(context.getString(R.string.cash_error), context);
    }

    @Override
    public void playMusic(String musicUri) {
        AndroidInterface.playMusic(musicUri, context);
    }
}
