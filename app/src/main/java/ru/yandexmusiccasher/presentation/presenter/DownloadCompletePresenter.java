package ru.yandexmusiccasher.presentation.presenter;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.interactor.DownloadCompleteInteractor;
import ru.yandexmusiccasher.domain.usecase.DownloadCompleteUseCase;
import ru.yandexmusiccasher.presentation.view.DownloadCompleteView;

/**
 * Created by Григорий Сенченок on 21.07.2018.
 */

public class DownloadCompletePresenter {

    private DownloadCompleteUseCase useCase;
    private DownloadCompleteView view;

    public DownloadCompletePresenter(SystemInterface system){
        useCase = new DownloadCompleteInteractor(system);
    }

    public void copyingError() {
        view.copyingError();
    }

    public void unableToDeleteCashFile() {
        view.unableToDeleteCashFile();
    }

    public void setView(DownloadCompleteView view) {
        this.view=view;
    }

    public void start() {
        String uri = view.getDownloadedFileUri();
        if(uri==null) view.downloadError();
        else useCase.fileDownloaded(uri, this);
    }
}
