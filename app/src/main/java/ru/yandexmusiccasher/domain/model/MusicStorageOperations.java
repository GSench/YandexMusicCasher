package ru.yandexmusiccasher.domain.model;

import java.io.FileNotFoundException;

/**
 * Created by grish on 10.08.2018.
 */

public interface MusicStorageOperations {

    public MusicCash getMusicCash();
    public MusicStorage getMusicStorage() throws FileNotFoundException;
    public void saveMusicStorage(MusicStorage storage);

}
