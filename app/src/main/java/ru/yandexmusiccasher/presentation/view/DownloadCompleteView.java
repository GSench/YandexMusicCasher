package ru.yandexmusiccasher.presentation.view;

import ru.yandexmusiccasher.domain.model.MusicFileCash;

/**
 * Created by grish on 23.07.2018.
 */

public interface DownloadCompleteView {
    public void downloadError();
    public void copyingError();
    public void unableToDeleteCashFile();
    public MusicFileCash getDownloadedFile();

}
