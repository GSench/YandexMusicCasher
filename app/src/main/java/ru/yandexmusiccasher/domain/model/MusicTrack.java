package ru.yandexmusiccasher.domain.model;

/**
 * Created by grish on 27.07.2018.
 */

public class MusicTrack {

    public String getAlbumID() {
        return albumID;
    }

    public String getTrackID() {
        return trackID;
    }

    public MusicTrack(String albumID, String trackID) {
        this.albumID = albumID;
        this.trackID = trackID;
    }

    public MusicTrack(String albumAndTrack){
        MusicTrack parsed = parse(albumAndTrack);
        this.albumID = parsed.getAlbumID();
        this.trackID = parsed.getTrackID();
    }

    private String albumID;
    private String trackID;

    public static MusicTrack parse(String s){
        if(s==null||"".equals(s)) return null;
        String trackID = s.substring(s.lastIndexOf("album") + "album".length());
        String albumID = s.substring(0, s.lastIndexOf("album"));
        albumID = albumID.substring(albumID.lastIndexOf("track") + "track".length());
        return new MusicTrack(albumID, trackID);
    }

    public static String toString(MusicTrack musicTrack){
        return "album"+musicTrack.getAlbumID()+"track"+musicTrack.getTrackID();
    }

    public String toString(){
        return toString(this);
    }

}
