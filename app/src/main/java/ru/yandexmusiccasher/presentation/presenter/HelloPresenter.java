package ru.yandexmusiccasher.presentation.presenter;

import ru.yandexmusiccasher.domain.interactor.PathInitializationInteractor;
import ru.yandexmusiccasher.domain.model.MusicStorage;
import ru.yandexmusiccasher.domain.model.MusicStorageOperations;
import ru.yandexmusiccasher.domain.presenters.PathInitPresenter;
import ru.yandexmusiccasher.presentation.view.HelloView;

/**
 * Created by grish on 20.08.2018.
 */

public class HelloPresenter implements PathInitPresenter {

    private PathInitializationInteractor useCase;
    private HelloView view;

    public HelloPresenter(MusicStorageOperations operations){
        this.useCase=new PathInitializationInteractor(operations, this);
    }

    public void setView(HelloView view){
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
}
