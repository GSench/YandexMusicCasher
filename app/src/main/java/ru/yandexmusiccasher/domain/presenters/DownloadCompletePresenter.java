package ru.yandexmusiccasher.domain.presenters;

/**
 * Created by grish on 11.08.2018.
 */

public interface DownloadCompletePresenter {

    public void copyingError();
    public void unableToDeleteCashFile();
    public void errorWritingTrackInfo();
    public void onYandexCaptcha();
    public void errorParsingTrackInfo();
    public void errorSettingMusicInfo();
}
