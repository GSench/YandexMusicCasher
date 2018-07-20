package ru.yandexmusiccasher.presentation.presenter;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.interactor.UrlReceivedInteractor;
import ru.yandexmusiccasher.domain.usecase.UrlReceivedUseCase;
import ru.yandexmusiccasher.domain.utils.HttpParams;
import ru.yandexmusiccasher.presentation.view.DownloadServiceView;

/**
 * Created by grish on 08.07.2018.
 */

public class UrlReceiverPresenter {

    private UrlReceivedUseCase useCase;
    private DownloadServiceView view;

    public UrlReceiverPresenter(SystemInterface system){
        useCase = new UrlReceivedInteractor(system);
    }

    public void setView(DownloadServiceView view){
        this.view=view;
    }

    public void urlReceived(String musicUrl){
        useCase.downloadTrackByUrl(musicUrl, this);
    }

    public void onYandexCaptcha(HttpParams params) {
        view.openYandexCaptcha(params);
    }

    public void onInternetError() {
        view.showInternetError();
    }

    public void onParseError() {
        view.showParseError();
    }

    public void onUndefinedPath() {
        view.showIndefinitePathError();
    }

}
