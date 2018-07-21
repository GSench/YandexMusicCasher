package ru.yandexmusiccasher.domain.interactor;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.usecase.DownloadCompleteUseCase;
import ru.yandexmusiccasher.presentation.presenter.DownloadCompletePresenter;

/**
 * Created by Григорий Сенченок on 21.07.2018.
 */

public class DownloadCompleteInteractor implements DownloadCompleteUseCase {

    private SystemInterface system;

    public DownloadCompleteInteractor(SystemInterface system){
        this.system=system;
    }

    @Override
    public void fileDownloaded(String uri, DownloadCompletePresenter presenter) {
        String musicDir = system.getSavedString(PathInitializationInteractor.PATH, null);
        try{
            system.copyFile(uri, musicDir);
        } catch (Exception e){
            e.printStackTrace();
            presenter.copyingError();
            return;
        }
        try {
            system.deleteMusicFileFromExtStorageDirByUri(uri);
        } catch (Exception e){
            e.printStackTrace();
            presenter.unableToDeleteCashFile();
        }
    }
}
