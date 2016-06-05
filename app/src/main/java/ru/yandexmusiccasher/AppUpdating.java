package ru.yandexmusiccasher;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by GSench on 04.06.2016.
 */
public class AppUpdating {

    private Context mContext;
    public static final String UPDATE_FILENAME = "update.apk";

    public AppUpdating(Context context){
        mContext=context;
    }

    public int getUpdateVersion(){
        InputStream inputStream = null;
        try{
            String urlStr = mContext.getString(R.string.version_url);
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            return Integer.parseInt(Tools.toString(inputStream));
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public void update(){
        String urlStr = mContext.getString(R.string.update_url);
        Uri source = Uri.parse(urlStr);
        DownloadManager.Request request = new DownloadManager.Request(source);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(mContext.getString(R.string.update_title));
        request.setDescription(mContext.getString(R.string.updating));
        File dir = new File(Environment.getExternalStorageDirectory(), YandexDownloadService.MUSIC_CASH);
        File file = new File(dir, UPDATE_FILENAME);
        file.delete();
        request.setDestinationUri(Uri.fromFile(file));
        DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}
