package ru.yandexmusiccasher.presentation.model;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import ru.yandexmusiccasher.domain.model.Directory;
import ru.yandexmusiccasher.domain.model.MFile;
import ru.yandexmusiccasher.domain.model.MusicTrack;
import ru.yandexmusiccasher.domain.model.SavingOperations;

/**
 * Created by grish on 27.07.2018.
 */

public class AndroidSavingOperations implements SavingOperations {

    private Context act;

    public AndroidSavingOperations(Context act){
        this.act=act;
    }

    @Override
    public void download(String name, Directory dir, String downloadUrl) {
        Uri source = Uri.parse(downloadUrl);
        String filename = name+".mp3";
        DownloadManager.Request request = new DownloadManager.Request(source);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(filename.substring(0, filename.lastIndexOf(".")));
        File filedir = new File(dir.toString());
        filedir.mkdirs();
        File file = new File(filedir, filename);
        request.setDestinationUri(Uri.fromFile(file));
        DownloadManager manager = (DownloadManager) act.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    public void copyFileToDir(MFile MFile, Directory dir) throws Exception {
        AndroidDir aDir = (AndroidDir) dir;
        AndroidFile aFile = (AndroidFile) MFile;
        AndroidFile copy = aDir.createFile(aFile.getName());

        InputStream in = null;
        OutputStream out = null;
        try {
            in = aFile.openInputStream(act);
            out = copy.openOutputStream(act);
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    @Override
    public MFile findMusicTrackInDirectory(MusicTrack track, Directory dir) {
        AndroidFile[] list = ((AndroidDir)dir).getListFiles();
        for (AndroidFile file: list)
            if(file.getName().endsWith(track.toString()+".mp3")) return file;
        return null;
    }


}
