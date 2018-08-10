package ru.yandexmusiccasher.domain.interactor;

import java.io.FileNotFoundException;

import ru.yandexmusiccasher.domain.model.MusicStorage;
import ru.yandexmusiccasher.domain.model.MusicStorageOperations;
import ru.yandexmusiccasher.domain.usecase.PathInitializationUseCase;
import ru.yandexmusiccasher.presentation.presenter.PathInitPresenter;

/**
 * Created by grish on 17.07.2018.
 */

public class PathInitializationInteractor implements PathInitializationUseCase {

    private MusicStorageOperations operations;
    private PathInitPresenter presenter;

    public PathInitializationInteractor(MusicStorageOperations operations, PathInitPresenter presenter){
        this.operations=operations;
        this.presenter=presenter;
    }

    @Override
    public void startInit() {
        try {
            operations.getMusicStorage();
        } catch (FileNotFoundException e) {
            presenter.pickPath();
            return;
        }
        presenter.pathInitialized();
    }

    @Override
    public void onStoragePicked(MusicStorage storage) {
        operations.saveMusicStorage(storage);
        presenter.pathInitialized();
    }
}
