package ru.yandexmusiccasher.presentation.model;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import ru.yandexmusiccasher.domain.model.MusicCash;
import ru.yandexmusiccasher.domain.model.MusicFileCash;

/**
 * Created by grish on 10.08.2018.
 */

public class AMusicCash extends MusicCash {

    private Context context;
    private File path;

    public AMusicCash(Context context){
        this.context=context;
        path = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
    }

    @Override
    public MusicFileCash findById(String id) {
        for (File file: path.listFiles()){
            if(file.getName().contains(id)) return new AMusicFileCash(id, file, context);
        }
        return null;
    }

    @Override
    public void download(String url, String title) {
        Uri source = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(source);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(title);
        File file = new File(path, title+".mp3");
        request.setDestinationUri(Uri.fromFile(file));
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}
