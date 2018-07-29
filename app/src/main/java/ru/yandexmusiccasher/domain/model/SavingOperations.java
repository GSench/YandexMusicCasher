package ru.yandexmusiccasher.domain.model;

/**
 * Created by grish on 27.07.2018.
 */

public interface SavingOperations {

    public void download(String name, Directory dir, String downloadUrl);
    public void copyFileToDir(MFile MFile, Directory dir) throws Exception;
    public MFile findMusicTrackInDirectory(MusicTrack track, Directory dir);

}
