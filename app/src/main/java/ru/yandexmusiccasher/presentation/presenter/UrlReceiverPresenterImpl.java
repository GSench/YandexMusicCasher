package ru.yandexmusiccasher.presentation.presenter;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.interactor.UrlReceivedInteractor;
import ru.yandexmusiccasher.domain.model.MusicFileCash;
import ru.yandexmusiccasher.domain.model.MusicStorageOperations;
import ru.yandexmusiccasher.domain.presenters.UrlReceiverPresenter;
import ru.yandexmusiccasher.presentation.view.DownloadServiceView;

/**
 * Created by grish on 08.07.2018.
 */

public class UrlReceiverPresenterImpl implements UrlReceiverPresenter {

    private UrlReceivedInteractor useCase;
    private DownloadServiceView view;

    public UrlReceiverPresenterImpl(SystemInterface system, MusicStorageOperations operations){
        useCase = new UrlReceivedInteractor(system, operations);
    }

    public void setView(DownloadServiceView view){
        this.view=view;
    }

    public void urlReceived(String musicUrl, int strategy){
        useCase.downloadTrackByUrl(musicUrl, strategy, this);
    }

    @Override
    public void onYandexCaptcha() {
        view.openYandexCaptcha();
    }

    @Override
    public void onInternetError() {
        view.showInternetError();
    }

    @Override
    public void onParseError() {
        view.showParseError();
    }

    @Override
    public void onMusicAlreadyInCash(MusicFileCash musicFileCash) {
        view.continueWithoutDownloading(musicFileCash);
    }

    @Override
    public void onUndefinedPath() {
        view.showIndefinitePathError();
    }

}
