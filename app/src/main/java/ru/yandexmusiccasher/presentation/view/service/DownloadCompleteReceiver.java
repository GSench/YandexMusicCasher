package ru.yandexmusiccasher.presentation.view.service;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by GSench on 02.06.2016.
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) return;
        Intent downloadCompleteIntent = new Intent(context, DownloadCompleteService.class);
        downloadCompleteIntent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
        context.startService(downloadCompleteIntent);
    }

}
