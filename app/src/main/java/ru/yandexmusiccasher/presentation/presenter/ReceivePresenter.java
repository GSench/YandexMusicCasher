package ru.yandexmusiccasher.presentation.presenter;

import ru.yandexmusiccasher.domain.interactor.PathInitializationInteractor;
import ru.yandexmusiccasher.domain.model.MusicStorage;
import ru.yandexmusiccasher.domain.model.MusicStorageOperations;
import ru.yandexmusiccasher.domain.presenters.PathInitPresenter;
import ru.yandexmusiccasher.presentation.view.UrlReceivedView;

/**
 * Created by grish on 17.07.2018.
 */

public class ReceivePresenter implements PathInitPresenter {

    private PathInitializationInteractor useCase;
    private UrlReceivedView view;

    public ReceivePresenter(MusicStorageOperations operations){
        this.useCase=new PathInitializationInteractor(operations, this);
    }

    public void setView(UrlReceivedView view){
        this.view=view;
    }

    public void start(){
        useCase.startInit();
    }

    public void onPathReceived(MusicStorage path){
        useCase.onStoragePicked(path);
    }

    public void onPathFailToReceive(){
        pickPath();
    }

    @Override
    public void pickPath() {
        view.pickPath();
    }

    @Override
    public void pathInitialized() {

    }

    public void download() {
        view.startDownloading();
    }

    public void downloadPlay() {
        view.startDownloadingPlaying();
    }

    public void play() {
        view.startPlaying();
    }
}
