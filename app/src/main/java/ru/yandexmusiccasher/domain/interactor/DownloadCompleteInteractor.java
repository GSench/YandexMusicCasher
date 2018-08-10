package ru.yandexmusiccasher.domain.interactor;

import ru.yandexmusiccasher.domain.model.MusicFile;
import ru.yandexmusiccasher.domain.model.MusicFileCash;
import ru.yandexmusiccasher.domain.model.MusicStorageOperations;
import ru.yandexmusiccasher.domain.usecase.DownloadCompleteUseCase;
import ru.yandexmusiccasher.presentation.presenter.DownloadCompletePresenter;

/**
 * Created by Григорий Сенченок on 21.07.2018.
 */

public class DownloadCompleteInteractor implements DownloadCompleteUseCase {

    private MusicStorageOperations operations;

    public DownloadCompleteInteractor(MusicStorageOperations operations){
        this.operations=operations;
    }

    @Override
    public void fileDownloaded(MusicFileCash fileCash, DownloadCompletePresenter presenter) {
        MusicFile music = null;
        try{
            music = fileCash.copyTo(operations.getMusicStorage());
        } catch (Exception e){
            e.printStackTrace();
            presenter.copyingError();
            return;
        }
        music.play();
        try {
            fileCash.delete();
        } catch (Exception e){
            e.printStackTrace();
            presenter.unableToDeleteCashFile();
        }
    }
}
