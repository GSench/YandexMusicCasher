package ru.yandexmusiccasher.domain.interactor;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.usecase.PathInitializationUseCase;
import ru.yandexmusiccasher.presentation.presenter.PathInitPresenter;

/**
 * Created by grish on 17.07.2018.
 */

public class PathInitializationInteractor implements PathInitializationUseCase {

    private static final String PATH = "music_dir";
    private SystemInterface system;
    private PathInitPresenter presenter;

    public PathInitializationInteractor(SystemInterface system, PathInitPresenter presenter){
        this.system=system;
        this.presenter=presenter;
    }

    @Override
    public void checkAndInitPath() {
        String savedPath = system.getSavedString(PATH, null);
        if(savedPath!=null&&system.checkUPathIsAvailable(savedPath)) presenter.pathInitialized();
        else presenter.pickPath();
    }

    @Override
    public void onPathPicked(String path) {
        system.saveString(PATH, path);
        presenter.pathInitialized();
    }
}
