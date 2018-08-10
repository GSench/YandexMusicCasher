package ru.yandexmusiccasher.domain.interactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import ru.yandexmusiccasher.domain.HttpHeadersManager;
import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.YandexCaptchaException;
import ru.yandexmusiccasher.domain.usecase.UrlReceivedUseCase;
import ru.yandexmusiccasher.domain.utils.HttpParams;
import ru.yandexmusiccasher.domain.utils.Pair;
import ru.yandexmusiccasher.presentation.presenter.UrlReceiverPresenter;

import static ru.yandexmusiccasher.domain.interactor.PathInitializationInteractor.PATH;

/**
 * Created by grish on 08.07.2018.
 */

public class UrlReceivedInteractor implements UrlReceivedUseCase {

    private SystemInterface system;
    private HttpHeadersManager httpHeadersManager;

    public UrlReceivedInteractor(SystemInterface system){
        this.system=system;
        httpHeadersManager = new HttpHeadersManager(system);
    }

    @Override
    public void downloadTrackByUrl(String url, UrlReceiverPresenter presenter) {

        String path = system.getSavedString(PATH, null);
        if(path==null){
            presenter.onUndefinedPath();
            return;
        }

        String trackID = (url.substring(url.indexOf("/album"))).replaceAll("/", "")+".mp3";

        String music = system.isMusicDownloaded(trackID, path);
        if(music!=null){
            presenter.playMusic(music);
            return;
        }


        String id = url.substring(url.lastIndexOf("/") + 1);
        String album = url.substring(url.indexOf("album/") + "album/".length());
        album = album.substring(0, album.indexOf("/"));

        String trackUrl, trackTitle;
        try {
            trackTitle = getTrack(id, album);
            trackUrl = getTrackUrl(id);
        } catch (YandexCaptchaException e) {
            presenter.onYandexCaptcha(httpHeadersManager.getHttpParams());
            return;
        } catch (IOException e) {
            e.printStackTrace();
            presenter.onInternetError();
            return;
        } catch (JSONException e) {
            e.printStackTrace();
            presenter.onParseError();
            return;
        }

        String cashPath = system.getCashPath();
        String filename = trackTitle+trackID;
        System.out.println("Cash path is: "+cashPath);
        System.out.println("filename is: "+filename);
        system.startDownloadingFile(trackUrl, cashPath, filename);
    }

    private String getTrackUrl(String trackId) throws IOException, JSONException, YandexCaptchaException {
        String trackInfoUrl = "https://music.yandex.ru/api/v2.1/handlers/track/"+trackId+"/track/download/m?hq=1";
        String trackInfo = yRequest(trackInfoUrl);
        JSONObject jObject = new JSONObject(trackInfo);
        String srcUrl = jObject.getString("src")+"&format=json";
        String downloadInfo = yRequest(srcUrl);
        String salt = "XGRlBW9FXlekgbPrRHuSiA";
        JSONObject jDownloadInfo = new JSONObject(downloadInfo);
        String hash = system.md5(salt + jDownloadInfo.getString("path").substring(1) + jDownloadInfo.getString("s"));
        return "https://"+jDownloadInfo.getString("host")+"/get-mp3/"+hash+"/"+jDownloadInfo.getString("ts")+jDownloadInfo.getString("path");
    }

    private String getTrack(String trackId, String albumId) throws IOException, JSONException, YandexCaptchaException {
        String url = "https://music.yandex.ru/handlers/track.jsx?track="+trackId+"%3A"+albumId;
        String response = yRequest(url);
        JSONObject jsonObject = new JSONObject(response);
        JSONObject jsonArtist = jsonObject.getJSONArray("artists").getJSONObject(0);
        String artist = jsonArtist.getString("name");
        JSONObject jsonTrack = jsonObject.getJSONObject("track");
        String title = jsonTrack.getString("title");
        String version = "";
        try {
            version = jsonTrack.getString("version");
        } catch (Exception e){}
        return artist+" - "+title+" "+version;
    }

    private boolean yandexCheck(String response){
        return response.contains("https://music.yandex.ru/captcha/");
    }

    private String yRequest(String url) throws YandexCaptchaException, IOException {
        Pair<byte[], HttpParams> response = system.httpGet(new URL(url), httpHeadersManager.getHttpParams());
        httpHeadersManager.updateHttpParams(response.s);
        String txt = new String(response.f, "UTF-8");
        if(yandexCheck(txt)){
            throw new YandexCaptchaException();
        }
        return txt;
    }

}
