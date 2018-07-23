package ru.yandexmusiccasher.presentation.view;

/**
 * Created by grish on 23.07.2018.
 */

public interface DownloadCompleteView {
    public String getDownloadedFileUri();
    public void downloadError();
    public void copyingError();
    public void unableToDeleteCashFile();
    public void playMusic(String musicUri);
}
