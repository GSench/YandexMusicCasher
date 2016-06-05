package ru.yandexmusiccasher;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;

/**
 * Created by GSench on 02.06.2016.
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String path = null, url = null;
        String action = intent.getAction();

        try {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor c = downloadManager.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);

                    //checking for update action
                    if(c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION)).equals(context.getString(R.string.updating))) return;

                    if (DownloadManager.STATUS_SUCCESSFUL != c.getInt(columnIndex)) {
                        ToastService.show(context.getString(R.string.download_error), context);
                        return;
                    } else {
                        int uriIndex = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                        int urlIndex = c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION);
                        path = c.getString(uriIndex);
                        url = c.getString(urlIndex);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            ToastService.show(context.getString(R.string.play_music_error), context);
            return;
        }

        if(TextUtils.isEmpty(path)||TextUtils.isEmpty(url)){
            ToastService.show(context.getString(R.string.play_music_error), context);
            return;
        }
        context
                .getSharedPreferences(YandexDownloadService.MUSIC_CASH, Context.MODE_PRIVATE)
                .edit()
                .putString(url, path)
                .apply();
        Tools.playMusic(path, context);
    }
}
