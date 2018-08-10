package ru.yandexmusiccasher.presentation.presenter;

import ru.yandexmusiccasher.domain.interactor.DownloadCompleteInteractor;
import ru.yandexmusiccasher.domain.model.MusicFileCash;
import ru.yandexmusiccasher.domain.model.MusicStorageOperations;
import ru.yandexmusiccasher.domain.presenters.DownloadCompletePresenter;
import ru.yandexmusiccasher.presentation.view.DownloadCompleteView;

/**
 * Created by Григорий Сенченок on 21.07.2018.
 */

public class DownloadCompletePresenterImpl implements DownloadCompletePresenter {

    private DownloadCompleteInteractor useCase;
    private DownloadCompleteView view;

    public DownloadCompletePresenterImpl(MusicStorageOperations operations){
        useCase = new DownloadCompleteInteractor(operations);
    }

    @Override
    public void copyingError() {
        view.copyingError();
    }

    @Override
    public void unableToDeleteCashFile() {
        view.unableToDeleteCashFile();
    }

    public void setView(DownloadCompleteView view) {
        this.view=view;
    }

    public void start() {
        MusicFileCash fileCash = view.getDownloadedFile();
        if(fileCash==null) view.downloadError();
        else useCase.fileDownloaded(fileCash, this);
    }

}
