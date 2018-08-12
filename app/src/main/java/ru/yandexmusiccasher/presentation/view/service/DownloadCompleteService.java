package ru.yandexmusiccasher.presentation.view.service;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import ru.yandexmusiccasher.R;
import ru.yandexmusiccasher.domain.model.MusicFileCash;
import ru.yandexmusiccasher.domain.model.MusicInfo;
import ru.yandexmusiccasher.presentation.AndroidInterface;
import ru.yandexmusiccasher.presentation.model.AMSOperations;
import ru.yandexmusiccasher.presentation.model.AMusicCash;
import ru.yandexmusiccasher.presentation.presenter.DownloadCompletePresenterImpl;
import ru.yandexmusiccasher.presentation.utils.IntentService;
import ru.yandexmusiccasher.presentation.utils.ToastService;
import ru.yandexmusiccasher.presentation.view.DownloadCompleteView;

/**
 * Created by grish on 11.08.2018.
 */

public class DownloadCompleteService extends IntentService implements DownloadCompleteView {

    private DownloadCompletePresenterImpl presenter;
    private Intent currentIntent;

    public DownloadCompleteService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        currentIntent = intent;
        if(presenter==null){
            presenter = new DownloadCompletePresenterImpl(new AMSOperations(this), new AndroidInterface(this));
            presenter.setView(this);
        }
        presenter.start();
    }

    @Override
    public MusicFileCash getDownloadedFile(){
        String path = null;
        long downloadId = currentIntent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
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
        return new AMusicCash(this).findById(MusicInfo.extractIds(Uri.parse(path).getLastPathSegment()));
    }

    @Override
    public void errorWritingTrackInfo() {
        ToastService.show(getString(R.string.error_writing_track_info), this);
    }

    @Override
    public void errorParsingTrackInfo() {
        ToastService.show(getString(R.string.error_parsing_track_info), this);
    }

    @Override
    public void errorSettingMusicInfo() {
        ToastService.show(getString(R.string.error_setting_music_info), this);
    }

    @Override
    public void downloadError() {
        ToastService.show(getString(R.string.download_error), this);
    }

    @Override
    public void copyingError() {
        ToastService.show(getString(R.string.copying_error), this);
    }

    @Override
    public void unableToDeleteCashFile() {
        ToastService.show(getString(R.string.cash_error), this);
    }

}
