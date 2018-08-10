package ru.yandexmusiccasher.domain.usecase;

import ru.yandexmusiccasher.domain.model.MusicStorage;

/**
 * Created by grish on 17.07.2018.
 */

public interface PathInitializationUseCase {

    public void startInit();
    public void onStoragePicked(MusicStorage storage);

}
