package ru.yandexmusiccasher.presentation.model;

import android.net.Uri;

import java.io.File;

import ru.yandexmusiccasher.domain.model.MusicTrack;

/**
 * Created by grish on 29.07.2018.
 */

public class MusicTrackConstructors {

    public static MusicTrack fromUrl(String originalUrl){
        return MusicTrack.parse(originalUrl);
    }

    public static MusicTrack fromFile(File file){
        if(file==null||!file.exists()||!file.isFile()) return null;
        return MusicTrack.parse(file.getName());
    }

    public static MusicTrack fromUri(Uri uri){
        if(uri==null) return null;
        return MusicTrack.parse(uri.getLastPathSegment());
    }

}
