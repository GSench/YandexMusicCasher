package ru.yandexmusiccasher.domain.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.yandexmusiccasher.domain.utils.Pair;

/**
 * Created by grish on 11.08.2018.
 */

public class MusicInfo {

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
        if(!m.matches()) return null;
        String raw = m.group();
        String album = raw.substring(0, "album".length());
        String track = raw.substring(raw.indexOf("k")+1);
        return new Pair<>(album, track);
    }

    public static String extractIds(String from){
        Pattern p = Pattern.compile("album\\d+track\\d+");
        Matcher m = p.matcher(from);
        if(!m.matches()) return null;
        return m.group();
    }

    public static String makeUrl(String album, String track){
        return "https://music.yandex.ru/album/"+album+"/track/"+track;
    }

}
