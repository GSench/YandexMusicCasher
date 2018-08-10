package ru.yandexmusiccasher.domain.model;

import java.io.IOException;

/**
 * Created by grish on 10.08.2018.
 */

public abstract class MusicFileCash extends MusicFile {

    public MusicFileCash(String id) {
        super(id);
    }

    public abstract void delete();

    public abstract MusicFile copyTo(MusicStorage music) throws IOException;

}
