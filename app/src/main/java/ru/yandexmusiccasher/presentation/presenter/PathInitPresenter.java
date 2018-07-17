package ru.yandexmusiccasher.presentation.presenter;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.interactor.PathInitializationInteractor;
import ru.yandexmusiccasher.domain.usecase.PathInitializationUseCase;
import ru.yandexmusiccasher.presentation.view.PathInitView;

/**
 * Created by grish on 17.07.2018.
 */

public class PathInitPresenter {

    private PathInitializationUseCase useCase;
    private PathInitView view;

    public PathInitPresenter(SystemInterface system){
        this.useCase=new PathInitializationInteractor(system, this);
    }

    public void setView(PathInitView view){
        this.view=view;
    }

    public void start(){
        useCase.checkAndInitPath();
    }

    public void onPathReceived(String path){
        useCase.onPathPicked(path);
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
