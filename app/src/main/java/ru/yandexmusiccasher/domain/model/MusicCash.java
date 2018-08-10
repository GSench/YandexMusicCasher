package ru.yandexmusiccasher.domain.model;

/**
 * Created by grish on 10.08.2018.
 */

public abstract class MusicCash {

    public abstract MusicFileCash findById(String id);
    public abstract void download(String url, String title);

}
