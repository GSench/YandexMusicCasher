package ru.yandexmusiccasher.presentation.presenter;

import ru.yandexmusiccasher.domain.interactor.PathInitializationInteractor;
import ru.yandexmusiccasher.domain.model.MusicStorage;
import ru.yandexmusiccasher.domain.model.MusicStorageOperations;
import ru.yandexmusiccasher.domain.presenters.PathInitPresenter;
import ru.yandexmusiccasher.presentation.view.PathInitView;

/**
 * Created by grish on 17.07.2018.
 */

public class PathInitPresenterImpl implements PathInitPresenter {

    private PathInitializationInteractor useCase;
    private PathInitView view;

    public PathInitPresenterImpl(MusicStorageOperations operations){
        this.useCase=new PathInitializationInteractor(operations, this);
    }

    public void setView(PathInitView view){
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
