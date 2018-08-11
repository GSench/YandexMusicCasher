package ru.yandexmusiccasher.domain.interactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.model.MusicFile;
import ru.yandexmusiccasher.domain.model.MusicStorage;
import ru.yandexmusiccasher.domain.model.MusicStorageOperations;
import ru.yandexmusiccasher.domain.presenters.UrlReceiverPresenter;
import ru.yandexmusiccasher.domain.services.Network;
import ru.yandexmusiccasher.domain.services.YandexCaptchaException;

/**
 * Created by grish on 08.07.2018.
 */

public class UrlReceivedInteractor {

    private SystemInterface system;
    private MusicStorageOperations sOperations;
    private Network network;

    public UrlReceivedInteractor(SystemInterface system, MusicStorageOperations sOperations){
        this.system=system;
        this.sOperations=sOperations;
        network = new Network(system);
    }

    public void downloadTrackByUrl(String url, UrlReceiverPresenter presenter) {

        MusicStorage storage;
        try {
            storage = sOperations.getMusicStorage();
        } catch (FileNotFoundException e) {
            presenter.onUndefinedPath();
            return;
        }

        String trackID = (url.substring(url.indexOf("/album"))).replaceAll("/", "");

        MusicFile musicFile = storage.findById(trackID);
        if(musicFile!=null){
            musicFile.play();
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
            presenter.onYandexCaptcha(network.getCurrentHttpParams());
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

        String filename = trackTitle+trackID;
        sOperations.getMusicCash().download(trackUrl, filename);
    }

    private String getTrackUrl(String trackId) throws IOException, JSONException, YandexCaptchaException {
        String trackInfoUrl = "https://music.yandex.ru/api/v2.1/handlers/track/"+trackId+"/track/download/m?hq=1";
        String trackInfo = network.yRequest(trackInfoUrl);
        JSONObject jObject = new JSONObject(trackInfo);
        String srcUrl = jObject.getString("src")+"&format=json";
        String downloadInfo = network.yRequest(srcUrl);
        String salt = "XGRlBW9FXlekgbPrRHuSiA";
        JSONObject jDownloadInfo = new JSONObject(downloadInfo);
        String hash = system.md5(salt + jDownloadInfo.getString("path").substring(1) + jDownloadInfo.getString("s"));
        return "https://"+jDownloadInfo.getString("host")+"/get-mp3/"+hash+"/"+jDownloadInfo.getString("ts")+jDownloadInfo.getString("path");
    }

    private String getTrack(String trackId, String albumId) throws IOException, JSONException, YandexCaptchaException {
        String url = "https://music.yandex.ru/handlers/track.jsx?track="+trackId+"%3A"+albumId;
        String response = network.yRequest(url);
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

}
