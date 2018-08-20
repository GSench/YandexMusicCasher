package ru.yandexmusiccasher.domain.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.yandexmusiccasher.domain.utils.Pair;

import static ru.yandexmusiccasher.domain.interactor.DownloadCompleteInteractor.DOWNLOAD;
import static ru.yandexmusiccasher.domain.interactor.DownloadCompleteInteractor.DOWNLOAD_PLAY;
import static ru.yandexmusiccasher.domain.interactor.DownloadCompleteInteractor.PLAY;

/**
 * Created by grish on 11.08.2018.
 */

public class MusicInfo {

    private static final String strategy = "strtg";
    public static final String play = "po";
    public static final String download = "do";
    public static final String download_play = "dp";

    public String track;
    public String artist;
    public String title;
    public String album;
    public String year;
    public String genre;
    public String comment;
    public String lyrics;
    public String composer;
    public String publisher;
    public String originalArtist;
    public String albumArtist;
    public String copyright;
    public String url;
    public String encoder;
    public byte[] albumArtwork;
    public String albumArtworkMime;

    public static Pair<String, String> getAlbumAndTrackIdsFromUrl(String url){
        String track = url.substring(url.lastIndexOf("/") + 1);
        String album = url.substring(url.indexOf("album/") + "album/".length());
        album = album.substring(0, album.indexOf("/"));
        return new Pair<>(album, track);
    }

    public static Pair<String, String> getAlbumAndTrackIds(String str){
        Pattern p = Pattern.compile("album\\d+track\\d+");
        Matcher m = p.matcher(str);
        if(!m.find()) return null;
        String raw = m.group();
        String album = raw.substring("album".length(), raw.indexOf("track"));
        String track = raw.substring(raw.indexOf("k")+1);
        return new Pair<>(album, track);
    }

    public static String extractIds(String from){
        Pattern p = Pattern.compile("album\\d+track\\d+");
        Matcher m = p.matcher(from);
        if(!m.find()) return null;
        return m.group();
    }

    public static String setStrategy(int strategy){
        String strategyName = download_play;
        switch (strategy){
            case PLAY: strategyName = play; break;
            case DOWNLOAD: strategyName = download; break;
            case DOWNLOAD_PLAY: strategyName = download_play; break;
        }
        return MusicInfo.strategy+strategyName;
    }

    public static int getStrategy(String from){
        Pattern p = Pattern.compile(strategy+"("+play+"|"+download+"|"+download_play+")");
        Matcher m = p.matcher(from);
        if(!m.find()) return DOWNLOAD_PLAY;
        String found = m.group();
        if(found.equals(strategy+play)) return PLAY;
        if(found.equals(strategy+download)) return DOWNLOAD;
        if(found.equals(strategy+download_play)) return DOWNLOAD_PLAY;
        return DOWNLOAD_PLAY;
    }

    public static String makeUrl(String album, String track){
        return "https://music.yandex.ru/album/"+album+"/track/"+track;
    }

}
