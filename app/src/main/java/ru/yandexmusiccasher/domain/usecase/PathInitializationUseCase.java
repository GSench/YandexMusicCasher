package ru.yandexmusiccasher.domain.usecase;

/**
 * Created by grish on 17.07.2018.
 */

public interface PathInitializationUseCase {

    public void checkAndInitPath();
    public void onPathPicked(String path);

}
