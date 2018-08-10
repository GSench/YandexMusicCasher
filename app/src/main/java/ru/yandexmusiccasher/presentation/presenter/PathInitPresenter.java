package ru.yandexmusiccasher.presentation.presenter;

import ru.yandexmusiccasher.domain.interactor.PathInitializationInteractor;
import ru.yandexmusiccasher.domain.model.MusicStorage;
import ru.yandexmusiccasher.domain.model.MusicStorageOperations;
import ru.yandexmusiccasher.domain.usecase.PathInitializationUseCase;
import ru.yandexmusiccasher.presentation.view.PathInitView;

/**
 * Created by grish on 17.07.2018.
 */

public class PathInitPresenter {

    private PathInitializationUseCase useCase;
    private PathInitView view;

    public PathInitPresenter(MusicStorageOperations operations){
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

    public void pickPath() {
        view.pickPath();
    }

    public void pathInitialized() {
        view.startDownloading();
    }
}
