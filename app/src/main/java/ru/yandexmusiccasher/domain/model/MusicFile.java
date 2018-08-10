package ru.yandexmusiccasher.domain.model;

/**
 * Created by grish on 10.08.2018.
 */

public abstract class MusicFile {

    public String getId() {
        return id;
    }

    public MusicFile(String id) {
        this.id = id;
    }

    private String id;

    public abstract void play();

}
