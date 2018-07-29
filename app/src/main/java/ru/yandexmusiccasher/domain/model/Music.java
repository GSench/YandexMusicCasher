package ru.yandexmusiccasher.domain.model;

/**
 * Created by grish on 29.07.2018.
 */

public class Music extends MusicTrack {

    private String title;
    private String album;
    private String artist;
    private String version;

    public String getTitle() {
        return title;
    }

    public Music setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public Music setAlbum(String album) {
        this.album = album;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public Music setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Music setVersion(String version) {
        this.version = version;
        return this;
    }

    public Music(String albumID, String trackID) {
        super(albumID, trackID);
    }

    public Music(MusicTrack musicTrack) {
        super(musicTrack.getAlbumID(), musicTrack.getTrackID());
    }

}
