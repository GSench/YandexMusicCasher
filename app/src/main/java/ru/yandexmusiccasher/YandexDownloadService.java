package ru.yandexmusiccasher;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by GSench on 01.06.2016.
 */
public class YandexDownloadService extends IntentService {

    public static final String MUSIC_CASH = "music_cash";

    CookieManager msCookieManager;

    String baseUrl;

    public YandexDownloadService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        baseUrl = intent.getStringExtra(Intent.EXTRA_TEXT);

        String cash = getCashedFilePath(baseUrl);
        if(cash!=null){
            Tools.playMusic(cash, this);
            return;
        }

        String id = baseUrl.substring(baseUrl.lastIndexOf("/") + 1);
        String album = baseUrl.substring(baseUrl.indexOf("album/") + "album/".length());
        album = album.substring(0, album.indexOf("/"));
        msCookieManager = new CookieManager(new MyCookieStore(this), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(msCookieManager);

        String trackUrl = null, trackTitle = null;
        try {
            trackTitle = getTrack(id, album);
            trackUrl = getTrackUrl(id);
        } catch (YandexCaptchaException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            ToastService.show(getString(R.string.parse_error), this);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
            ToastService.show(getString(R.string.parse_error), this);
            return;
        }
        downloadTrack(trackTitle, trackUrl);
    }

    private String getCashedFilePath(String webPath){
        return getSharedPreferences(MUSIC_CASH, MODE_PRIVATE).getString(webPath, null);
    }

    private String getTrackUrl(String trackId) throws IOException, JSONException, YandexCaptchaException {
        String trackInfoUrl = "https://music.yandex.ru/api/v2.0/handlers/track/"+trackId+"/download/m?hq=1";
        String trackInfo = Tools.toString(Tools.download(trackInfoUrl, msCookieManager));
        if(yandexCheck(trackInfo)){
            throw new YandexCaptchaException();
        }

        JSONObject jObject = new JSONObject(trackInfo);
        String src = jObject.getString("src");

        String downloadInfo = Tools.toString(Tools.download(src + "&format=json", msCookieManager));
        if(yandexCheck(downloadInfo)){
            throw new YandexCaptchaException();
        }
        String salt = "XGRlBW9FXlekgbPrRHuSiA";

        JSONObject jDownloadInfo = new JSONObject(downloadInfo);

        String hash = Tools.md5(salt + jDownloadInfo.getString("path").substring(1) + jDownloadInfo.getString("s"));

        return "https://"+jDownloadInfo.getString("host")+"/get-mp3/"+hash+"/"+jDownloadInfo.getString("ts")+jDownloadInfo.getString("path");
    }

    private String getTrack(String trackId, String albumId) throws IOException, JSONException, YandexCaptchaException {

        String url = "https://music.yandex.ru/handlers/track.jsx?track="+trackId+"%3A"+albumId;
        String response = Tools.toString(Tools.download(url, msCookieManager));
        if(yandexCheck(response)){
            throw new YandexCaptchaException();
        }
        JSONObject jsonObject = new JSONObject(response);
        JSONObject jsonArtist = jsonObject.getJSONArray("artists").getJSONObject(0);
        String artist = jsonArtist.getString("name");

        JSONObject jsonTrack = jsonObject.getJSONObject("track");
        String title = jsonTrack.getString("title");
        String version = "";
        try {
            version = jsonTrack.getString("version");
        } catch (Exception e){}
        return artist+" - "+title+version;
    }

    private boolean yandexCheck(String response){
        if(response.contains("https://music.yandex.ru/captcha/")){
            breakQueue();
            startActivity(new Intent(this, YandexVerifyActivity.class).putExtra(Intent.EXTRA_TEXT, baseUrl).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        } return false;
    }

    private void downloadTrack(String title, String url){
        Uri source = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(source);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(title);
        request.setDescription(baseUrl);
        File musicDir = new File(Environment.getExternalStorageDirectory(), MUSIC_CASH);
        musicDir.mkdirs();
        File musicFile = new File(musicDir, title+".mp3");
        request.setDestinationUri(Uri.fromFile(musicFile));
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private class YandexCaptchaException extends Exception{

    }

}
