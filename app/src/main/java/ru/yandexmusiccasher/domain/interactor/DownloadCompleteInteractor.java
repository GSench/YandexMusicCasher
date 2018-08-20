package ru.yandexmusiccasher.domain.interactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.model.MusicFile;
import ru.yandexmusiccasher.domain.model.MusicFileCash;
import ru.yandexmusiccasher.domain.model.MusicInfo;
import ru.yandexmusiccasher.domain.model.MusicStorageOperations;
import ru.yandexmusiccasher.domain.presenters.DownloadCompletePresenter;
import ru.yandexmusiccasher.domain.services.Network;
import ru.yandexmusiccasher.domain.services.YandexCaptchaException;
import ru.yandexmusiccasher.domain.utils.Pair;

/**
 * Created by Григорий Сенченок on 21.07.2018.
 */

public class DownloadCompleteInteractor {

    public static final int PLAY = 1;
    public static final int DOWNLOAD = 2;
    public static final int DOWNLOAD_PLAY = 3;

    private MusicStorageOperations operations;
    private Network network;

    public DownloadCompleteInteractor(MusicStorageOperations operations, SystemInterface system){
        this.operations=operations;
        network = new Network(system);
    }

    public void fileDownloaded(MusicFileCash fileCash, DownloadCompletePresenter presenter) {
        int strategy = MusicInfo.getStrategy(fileCash.getName());
        try {
            MusicInfo musicInfo = getMusicInfo(fileCash.getId());
            fileCash.setMusicInfo(musicInfo);
        } catch (IOException e) {
            e.printStackTrace();
            presenter.errorWritingTrackInfo();
        } catch (YandexCaptchaException e) {
            e.printStackTrace();
            presenter.onYandexCaptcha(network.getCurrentHttpParams());
        } catch (JSONException e) {
            e.printStackTrace();
            presenter.errorParsingTrackInfo();
        } catch (Exception e) {
            e.printStackTrace();
            presenter.errorSettingMusicInfo();
        }
        if(strategy==PLAY){
            fileCash.play();
            return;
        }
        MusicFile music = null;
        try{
            music = fileCash.copyTo(operations.getMusicStorage());
        } catch (Exception e){
            e.printStackTrace();
            presenter.copyingError();
            return;
        }
        try {
            fileCash.delete();
        } catch (Exception e){
            e.printStackTrace();
            presenter.unableToDeleteCashFile();
        }
        if(strategy==DOWNLOAD_PLAY){
            music.play();
        }
    }

    private MusicInfo getMusicInfo(String id) throws IOException, YandexCaptchaException, JSONException {
        Pair<String, String> track = MusicInfo.getAlbumAndTrackIds(id);
        String url = "https://music.yandex.ru/handlers/track.jsx?track="+track.s+"%3A"+track.f;
        String response = network.yRequest(url);
        JSONObject jsonObject = new JSONObject(response);
        return convert(jsonObject, track.s, track.f);
    }

    private MusicInfo convert(JSONObject json, String trackId, String albumId){

        MusicInfo musicInfo = new MusicInfo();

        //comments
        musicInfo.comment = "Limitation of Liability";
        //publisher
        musicInfo.publisher = "Yandex.Music";
        //url
        musicInfo.url = MusicInfo.makeUrl(albumId, trackId);

        try {

            JSONObject track = json.getJSONObject("track");

            //title with version
            String version = null;
            try { version = track.getString("version"); } catch (Exception e){}
            musicInfo.title = track.getString("title") + (version!=null ? " "+version.trim() : "");

            //artist or artists
            try {
                JSONArray arists = track.getJSONArray("artists");
                musicInfo.artist = arists.getJSONObject(0).getString("name");
                for(int i=1; i<arists.length(); i++) musicInfo.artist = musicInfo.artist+", "+arists.getJSONObject(i).getString("name");
                musicInfo.originalArtist = musicInfo.artist;
            } catch (Exception e){}

            //album
            try {
                JSONObject album = track.getJSONArray("albums").getJSONObject(0);
                //album
                musicInfo.album = album.getString("title");

                //album artwork
                try {
                    String url = album.getString("coverUri");
                    url = "http://"+url.substring(0, url.indexOf("%"))+"400x400";
                    musicInfo.albumArtwork = network.yRawRequest(url);
                    musicInfo.albumArtworkMime = "image/jpeg";
                } catch (Exception e){e.printStackTrace();}

                //year
                musicInfo.year = album.getString("year");
                //genre
                musicInfo.genre = album.getString("genre");
                //track position
                musicInfo.track = album.getJSONObject("trackPosition").getInt("index")+"";
                //album artist
                JSONArray arists = album.getJSONArray("artists");
                musicInfo.albumArtist = arists.getJSONObject(0).getString("name");
                for(int i=1; i<arists.length(); i++) musicInfo.albumArtist = musicInfo.albumArtist+", "+arists.getJSONObject(i).getString("name");
            } catch (Exception e){}

            //lyrics
            try{
                musicInfo.lyrics = json.getJSONArray("lyric").getJSONObject(0).getString("fullLyrics");
            } catch (Exception e){}

        } catch (JSONException e) {
            e.printStackTrace(); //should be safe
        }
        return musicInfo;
    }

}
