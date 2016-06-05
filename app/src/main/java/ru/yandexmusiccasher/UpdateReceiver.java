package ru.yandexmusiccasher;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by GSench on 04.06.2016.
 */
public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor c = downloadManager.query(query);
            if (c.moveToFirst()) {
                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if(!c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION)).equals(context.getString(R.string.updating))) return;

                if (DownloadManager.STATUS_SUCCESSFUL != c.getInt(columnIndex)) {
                    Toast.makeText(context, context.getString(R.string.update_error), Toast.LENGTH_SHORT).show();
                } else {
                    int uriIndex = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    String pathUri = c.getString(uriIndex);
                    Intent update = new Intent(Intent.ACTION_VIEW);
                    update.setDataAndType(Uri.parse(pathUri), "application/vnd.android.package-archive");
                    update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(update);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.update_error), Toast.LENGTH_SHORT).show();
        }
    }
}
